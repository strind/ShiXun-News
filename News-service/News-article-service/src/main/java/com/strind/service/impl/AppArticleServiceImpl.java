package com.strind.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.common.ProtostuffUtil;
import com.strind.commonInterface.article.UpdateArticleInfo;
import com.strind.commonInterface.user.IsFan;
import com.strind.constants.ArticleConstants;
import com.strind.exception.CustomException;
import com.strind.mapper.AppArticleCollectionMapper;
import com.strind.mapper.AppArticleConfigMapper;
import com.strind.mapper.AppArticleMapper;
import com.strind.model.article.dtos.ArticleCollectionDto;
import com.strind.model.article.dtos.ArticleHomeDto;
import com.strind.model.article.dtos.ArticleInfoDto;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.article.pojos.AppArticleCollection;
import com.strind.model.article.pojos.AppArticleConfig;
import com.strind.model.article.vos.ArticleInfoVO;
import com.strind.model.article.vos.HotArticleVO;
import com.strind.model.common.RespResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.rabbitmq.RabbitMQConstants;
import com.strind.redis.CacheService;
import com.strind.redis.RedisConstants;
import com.strind.service.AppArticleService;
import com.strind.thread.AppThreadLocalUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author strind
 * @version 1.0
 * @description 文章首页服务
 * @date 2024/3/21 21:06
 */

@Service
public class AppArticleServiceImpl extends ServiceImpl<AppArticleMapper, AppArticle> implements AppArticleService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppArticleMapper appArticleMapper;

    @Autowired
    private AppArticleConfigMapper appArticleConfigMapper;

    @Autowired
    private AppArticleCollectionMapper appArticleCollectionMapper;

    @DubboReference(check = false)
    private IsFan isFan;

    @DubboReference(check = false)
    private UpdateArticleInfo updateArticleInfo;

    private static final Short YES = 0;
    private static final Short NO = 1;

    @Override
    public RespResult load(ArticleHomeDto dto, Short type, boolean firstPage) {
        checkOrInit(dto, type);
        // 加载首页，首先查询缓存。
        if (firstPage){
            String key = RedisConstants.HOT_ARTICLE_CACHE + ArticleConstants.DEFAULT_TAG;
            String s = cacheService.get(key);
            if (StringUtils.isNotBlank(s)){
                List<HotArticleVO> hotArticleVOS = JSONObject.parseArray(s, HotArticleVO.class);
                return RespResult.okResult(hotArticleVOS);
            }
        }
        List<AppArticle> appArticles = appArticleMapper.loadArticleList(dto, type);
        return RespResult.okResult(appArticles);
    }


    @Override
    public RespResult loadmore(ArticleHomeDto dto, Short type) {
        return load(dto,type, false);
    }

    /**
     * @param dto  参数
     * @param type 类型
     * @return
     */
    @Override
    public RespResult loadnew(ArticleHomeDto dto, Short type) {
        return load(dto,type, false);
    }

    /**
     * @param dto 参数
     * @return
     */
    @Override
    public RespResult loadArticleInfo(ArticleInfoDto dto) {
        // 校验参数
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer userId = AppThreadLocalUtil.getUser().getId();
        // TODO: 2024/4/6 暂且先通过redis组装结果
        Short isLike = (short) (cacheService.sIsMember(RedisConstants.LIKE_ARTICLE_ + dto.getArticleId(),String.valueOf(userId)) ? 0 : 1);
        Short isUnlike = (short) (cacheService.sIsMember(RedisConstants.UN_LIKE_ARTICLE + dto.getArticleId(),String.valueOf(userId)) ? 0 : 1);
        Short isCollection = (short) (cacheService.sIsMember(RedisConstants.COLLECTION + dto.getArticleId(),String.valueOf(userId)) ? 0 : 1);
        Short isf = (short) (isFan.isFan(userId,dto.getAuthorId()) ? 0 : 1);
        ArticleInfoVO data = ArticleInfoVO.builder().islike(isLike)
            .isfollow(isf).isunlike(isUnlike).iscollection(isCollection).build();
        return RespResult.okResult(data);
    }


    /**
     * 收藏，可能频繁操作，放入redis
     *
     * @param dto
     * @return
     */
    @Override
    public RespResult collection(ArticleCollectionDto dto) {
        // 校验参数
        if (dto == null || dto.getEntryId() == null || dto.getOperation() == null
            || dto.getPublishedTime() == null || dto.getType() == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer userId = AppThreadLocalUtil.getUser().getId();
        if (YES.equals(dto.getOperation())){
            // 收藏
            Set<String> set = null;
            if (cacheService.hExists(RedisConstants.COLLECTION,dto.getEntryId().toString())){
                String o = (String) cacheService.hGet(RedisConstants.COLLECTION, dto.getEntryId().toString());
                set = JSON.parseObject(o, Set.class);
            }else {
                set = new HashSet<>();
            }
            set.add(userId.toString());
            cacheService.hPut(RedisConstants.COLLECTION, dto.getEntryId().toString(),JSON.toJSONString(set));
        }else {
            // 取消收藏
            if (cacheService.hExists(RedisConstants.COLLECTION,dto.getEntryId().toString())){
                // 在缓存中，未同步到数据库，直接删除即可
                String o = (String) cacheService.hGet(RedisConstants.COLLECTION, dto.getEntryId().toString());
                Set<String> set = JSON.parseObject(o, Set.class);
                if (set.contains(userId.toString())){
                    set.remove(userId.toString());
                    cacheService.hPut(RedisConstants.COLLECTION, dto.getEntryId().toString(),JSON.toJSONString(set));
                }else {
                    // 从数据库删除
                    AppArticleCollection collection = AppArticleCollection.builder()
                        .userId(userId)
                        .entryId(dto.getEntryId())
                        .build();
                    appArticleCollectionMapper.delete(Wrappers.<AppArticleCollection>lambdaQuery()
                        .eq(AppArticleCollection::getUserId, collection.getUserId())
                        .eq(AppArticleCollection::getEntryId, collection.getEntryId())
                    );
                }

            }else {
                // 从数据库删除
                AppArticleCollection collection = AppArticleCollection.builder()
                    .userId(userId)
                    .entryId(dto.getEntryId())
                    .build();
                appArticleCollectionMapper.delete(Wrappers.<AppArticleCollection>lambdaQuery()
                    .eq(AppArticleCollection::getUserId, collection.getUserId())
                    .eq(AppArticleCollection::getEntryId, collection.getEntryId())
                );
            }
        }
        return RespResult.okResult("success");
    }

    /**
     * 参数校验函数
     * @param dto
     * @param type
     */
    private static void checkOrInit(ArticleHomeDto dto, Short type) {
        if (Objects.isNull(dto)){
            RespResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 开始参数校验
        Integer size = dto.getSize();
        if (size <= 0 || size > ArticleConstants.MAX_PAGE_SIZE){
            dto.setSize(ArticleConstants.DEFAULT_PAGE_SIZE);
        }
        if (type == null || (!ArticleConstants.LOADTYPE_LOAD_MORE.equals(type) && !ArticleConstants.LOADTYPE_LOAD_NEW.equals(
            type))){
            // 默认加载更多
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        if (dto.getTag() == null){
            // 默认是首页
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
    }

    // TODO: 2024/4/2 监听队列，更改文章的上下架状态
    @RabbitListener(queues = RabbitMQConstants.DOWN_OR_UP)
    public void downOrUpListener(Message message){
        if (message == null){
            return;
        }
        byte[] body = message.getBody();
        String str = ProtostuffUtil.deserialize(body, String.class);
        Map<String,String> map = JSON.parseObject(str, Map.class);
        if (map != null && !map.isEmpty()){
            Long articleId = Long.valueOf(map.get("articleId"));
            Short type = Short.valueOf(map.get("type"));
            AppArticleConfig appArticleConfig = new AppArticleConfig();
            appArticleConfig.setArticleId(articleId);
            appArticleConfig.setIsDown(Short.valueOf("0").equals(type));
            // 更新article的配置文件
            appArticleConfigMapper.update(appArticleConfig, Wrappers.<AppArticleConfig>lambdaUpdate()
                .eq(AppArticleConfig::getArticleId, appArticleConfig.getArticleId()));
        }
    }

    /**
     * 定时更新文章的收藏数量，及同步数据到数据库
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void refreshReadCount(){
        Map<Object, Object> map = cacheService.hGetAll(RedisConstants.COLLECTION);
        if (!map.isEmpty()){
            List<AppArticle> list = new ArrayList<>();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Long articleId = (Long) entry.getKey();
                Set<String> count = (Set<String>) entry.getValue();
                AppArticle appArticle = new AppArticle();
                appArticle.setId(articleId);
                appArticle.setCollection(count.size());
                list.add(appArticle);
                cacheService.hDelete(RedisConstants.READ,articleId);
                for (String user_id : count) {
                    AppArticleCollection collection = AppArticleCollection.builder()
                        .userId(Integer.valueOf(user_id))
                        .entryId(articleId)
                        .type(Short.valueOf("0"))
                        .collectionTime(new Date())
                        .publishedTime(new Date())
                        .build();
                   appArticleCollectionMapper.insert(collection);
                }
            }
            updateArticleInfo.updateArticlesInfo(list, ArticleConstants.VIEWS_TYPE);
        }
    }



}
