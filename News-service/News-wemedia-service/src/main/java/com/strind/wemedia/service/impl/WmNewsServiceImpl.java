package com.strind.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.client.Tess4jClient;
import com.strind.common.ProtostuffUtil;
import com.strind.common.SensitiveWordUtil;
import com.strind.commonInterface.SaveArticle;
import com.strind.constants.WmNewsConstants;
import com.strind.constants.WmmediaConstants;
import com.strind.exception.CustomException;
import com.strind.file.service.FileStorageService;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.common.RespResult;
import com.strind.model.common.dtos.PageResponseResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.model.wemedia.dtos.WmNewsDto;
import com.strind.model.wemedia.dtos.WmNewsPageDto;
import com.strind.model.wemedia.pojos.WmMaterial;
import com.strind.model.wemedia.pojos.WmNews;
import com.strind.model.wemedia.pojos.WmNewsMaterial;
import com.strind.thread.WmmediaThreadLocalUtil;
import com.strind.wemedia.mapper.*;
import com.strind.wemedia.service.WmNewsService;
import com.strind.wemedia.task.ExecuteTaskPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:32
 */
@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Tess4jClient tess4jClient;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WmUserMapper wmUserMapper;


    @DubboReference
    private SaveArticle saveArticle;

    @Autowired
    private WmNewsMapper wmNewsMapper;


    /**
     * 查询内容列表
     *
     * @param dto
     * @return
     */
    @Override
    public RespResult findList(WmNewsPageDto dto) {
        // 参数校验
        dto.checkParam();
        // 分页条件查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 状态
        if (dto.getStatus() != null){
            queryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        // 频道
        if (dto.getChannelId() != null){
            queryWrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }

        // 时间范围
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null){
            queryWrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());

        }

        // 模糊查询
        if (StringUtils.isNotBlank(dto.getKeyword())){
            queryWrapper.like(WmNews::getTitle,dto.getKeyword());
        }

        // 当前用户
        queryWrapper.eq(WmNews::getUserId, WmmediaThreadLocalUtil.getUser().getId());

        // 发布时间倒序
        queryWrapper.orderByDesc(WmNews::getPublishTime);

        page = page(page,queryWrapper);

        // 结果封装
        RespResult result = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());

        result.setData(page.getRecords());
        return result;

    }

    /**
     * 提交或保存为草稿
     *
     * @param dto
     * @return
     */
    @Override
    public RespResult submit(WmNewsDto dto) {
        // 参数校验
        if (dto == null){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        wmNews.setUserId(WmmediaThreadLocalUtil.getUser().getId());
        wmNews.setSubmitedTime(new Date());
        wmNews.setCreatedTime(new Date());
        // 图片特殊处理
        if (dto.getImages() != null && dto.getImages().size() > 0){
            wmNews.setImages(StringUtils.join(dto.getImages(),","));
        }
        if (wmNews.getType().equals(WmmediaConstants.WM_NEWS_TYPE_AUTO)){
            wmNews.setType(null);
        }
        saveOrUpdateWmNews(wmNews);
        if (WmNewsConstants.DRAFT.equals(dto.getStatus())){
            // 存草稿
            return RespResult.okResult("ok");
        }
        // 提交审核

        // 获取图片素材，添加的内容中与素材文章关系表中
        List<String> images = parseImages(dto.getContent());
        saveInRelationTable(images,wmNews.getId());
        // 添加封面与文章的关系
        saveRelationOfCover(dto,wmNews, images);

        // TODO: 2024/3/25 这里很明显不合理，需要改进, 初始化敏感词库表
        List<String> sensitive = wmSensitiveMapper.getSensitive();
        SensitiveWordUtil.initMap(sensitive);
        String authorName = wmUserMapper.selectOnlyName(wmNews.getUserId());
        wmNews.setAuthorName(authorName);
        String channelName = wmChannelMapper.selectOnlyName(wmNews.getChannelId());
        wmNews.setChannelName(channelName);
        Long articleId = wmNewsMapper.getArticleId(wmNews.getId());
        wmNews.setArticleId(articleId);
        // TODO: 2024/3/24 提交审核
        // 提交线程池，异步处理
        log.info("提交任务交给线程池处理");
        ExecuteTaskPool.addTask(new AutoScanTask(wmNews));

        return RespResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelationOfCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();

        // 封面类型是自动
        if(dto.getType().equals(WmmediaConstants.WM_NEWS_TYPE_AUTO)){

            log.info("封面是自动，且image.size = {}", images.size());
            if (materials.size() >= 3){
                // 多图
                wmNews.setType(WmmediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if (materials.size() >= 1){
                wmNews.setType(WmmediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else {
                // 无图
                wmNews.setType(WmmediaConstants.WM_NEWS_NONE_IMAGE);
            }

            // 修改文章
            if (images != null && !images.isEmpty()){
                wmNews.setImages(StringUtils.join(images,","));
            }
            updateById(wmNews);
        }

        if (images != null && !images.isEmpty()){
            saveRelation(images,wmNews.getId(),WmmediaConstants.COVER_TYPE);
        }
    }

    private void saveInRelationTable(List<String> images, Integer newsId) {
        saveRelation(images,newsId, WmmediaConstants.CONTENT_TYPE);
    }

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Override
    public RespResult getNews(Integer id) {
        if (id == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews news = getOne(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getId, id));
        if (news == null){
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return RespResult.okResult(news);
    }

    /**
     * 删除文章（仅自媒体端）
     *
     * @param id
     * @return
     */
    @Override
    public RespResult delNewsById(Integer id) {
        if (id == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 先删除关系
        wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,id));

        // 在删除文章
        removeById(id);
        return RespResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelation(List<String> images, Integer newsId, Short type) {
        if (images != null && images.size() > 0){
            List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(
                Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, images));
            if (wmMaterials == null || wmMaterials.isEmpty() || wmMaterials.size() != images.size()) {
                throw  new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }
            List<Integer> ids = wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
            wmNewsMaterialMapper.saveRelation(ids,newsId,type);
        }
    }

    private List<String> parseImages(String content) {
        List<String> images = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if (map.get("type").equals("image")) {
                String imageUrl = (String) map.get("value");
                images.add(imageUrl);
            }
        }
        return images;
    }

    private void saveOrUpdateWmNews(WmNews wmNews){
        Integer id = wmNews.getId();
        if (id == null){
            save(wmNews);
        }else {
            log.info("文章已存在，更新文章");
            // 将素材与文章的关系删除
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            updateById(wmNews);
        }

    }

    //@RabbitListener(queues = RabbitMQConstants.CHANGE_WMNEWS_STATUS_QUEUE)
    public void listenQueue4UpdateWmNews(byte[] content){
        WmNews wmNews = ProtostuffUtil.deserialize(content, WmNews.class);
        // 并不涉及内容的更改
        updateById(wmNews);
    }


    class AutoScanTask implements Runnable{

        private WmNews wmNews;

        public AutoScanTask(WmNews wmNews) {
            this.wmNews = wmNews;
        }

        @Override
        public void run() {
            log.info("开始进行文章的自动审核 {}", wmNews.getId());
            // 根据发布时间进行分类
            long publishTime = wmNews.getPublishTime().getTime();
            // 开始审核
            if (wmNews == null){
                throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
            }

            synchronized (wmNews){
                // 判断是否是待审核状态
                if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
                    // 提取文本和图片
                    Map<String, Object> imagesAndText = getImgesAndText(wmNews);

                    log.info("文章正式开始审核 + {}",wmNews.getId());
                    Map<String, Integer> result = SensitiveWordUtil.matchWords((String) imagesAndText.get("content"));
                    if (!result.isEmpty()){
                        // 审核失败, 放入队列，异步更新
                        wmNews.setStatus(WmNews.Status.FAIL.getCode());
                        String rean = StringUtils.join(result.keySet().stream().collect(Collectors.toList()), ",");
                        wmNews.setReason("文本含有" + rean);
                        update(wmNews,Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,wmNews.getId()));
                        return;
                    }
                    // 图片审核
                    List<String> images = (List<String>) imagesAndText.get("images");
                    for (String image : images) {
                        try {
                            byte[] bytes = fileStorageService.downLoadFile(image);
                            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                            BufferedImage imageFile = ImageIO.read(in);

                            String sa = tess4jClient.doOCR(imageFile);
                            Map<String, Integer> map = SensitiveWordUtil.matchWords(sa);
                            if (!map.isEmpty()){
                                // TODO: 2024/3/24  审核失败, 放入队列，异步更新
                                wmNews.setStatus(WmNews.Status.FAIL.getCode());
                                String rean = StringUtils.join(result.keySet().stream().collect(Collectors.toList()), ",");
                                wmNews.setReason("图片含有" + rean);
                                update(wmNews,Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,wmNews.getId()));
                                return;
                            }
                        }catch (Exception e){
                            log.error("图片审核失败：{}", image);
                        }
                    }

                    // TODO: 2024/3/24 需要使用Dubbo 审核成功，保存文章在app端
                    AppArticle appArticle = new AppArticle();
                    BeanUtils.copyProperties(wmNews,appArticle);
                    appArticle.setAuthorId(Long.valueOf(wmNews.getUserId()));
                    appArticle.setAuthorName(wmNews.getAuthorName());
                    appArticle.setChannelName(wmNews.getChannelName());
                    appArticle.setLayout(wmNews.getType());
                    appArticle.setContent(wmNews.getContent());
                    if (wmNews.getArticleId() != null){
                        // 由对于的文章存在，执行更新
                        saveArticle.updateArticle(appArticle);
                    }else {
                        // 文章不存在，执行保存
                        log.info("调用远程服务，保存文章");
                        Long articleId = saveArticle.saveArticle(appArticle);
                        // 将文章id放入mq, 在app更改
                        wmNews.setArticleId(articleId);
                    }
                    // 判断是否发布
                    long currentTime = new Date().getTime();
                    if (publishTime <= currentTime){
                        // 放入mq, 去更改文章状态（写入app端文章的id）
                        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
                        update(wmNews,Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,wmNews.getId()));
                        log.info("文章 {} 的审核正式结束", wmNews.getId());
                    }else {
                        // TODO: 2024/3/27 定时发布功能
                        wmNews.setStatus(WmNews.Status.SUCCESS.getCode());
                        update(wmNews,Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,wmNews.getId()));
                        log.info("文章 {} 的审核正式结束", wmNews.getId());
                    }
                }
            }
        }


        // 提取文本和图片
        private Map<String, Object> getImgesAndText(WmNews wmNews) {
            StringBuilder sb = new StringBuilder();
            List<String> images = new ArrayList<>();

            if (StringUtils.isNotBlank(wmNews.getContent())){
                List<Map> maps = JSON.parseArray(wmNews.getContent(), Map.class);
                for (Map map : maps) {
                    if (map.get("type").equals("text")){
                        sb.append(map.get("value"));
                    }
                    if (map.get("type").equals("image")){
                        images.add((String) map.get("value"));
                    }
                }
            }

            // 提取封面图片
            if (StringUtils.isNotBlank(wmNews.getImages())){
                String[] split = wmNews.getImages().split(",");
                images.addAll(Arrays.asList(split));
            }
            Map<String, Object> ans = new HashMap<>();
            ans.put("content",sb.toString());
            ans.put("images",images);
            return ans;
        }
    }

}
