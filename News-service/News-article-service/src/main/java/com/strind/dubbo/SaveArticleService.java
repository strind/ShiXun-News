package com.strind.dubbo;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.strind.commonInterface.article.SaveArticle;
import com.strind.file.service.FileStorageService;
import com.strind.mapper.AppArticleConfigMapper;
import com.strind.mapper.AppArticleContentMapper;
import com.strind.mapper.AppArticleMapper;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.article.pojos.AppArticleConfig;
import com.strind.model.article.pojos.AppArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author strind
 * @version 1.0
 * @description 保存文章
 * @date 2024/3/27 10:15
 */
@DubboService
@Component
@Slf4j
@Transactional
public class SaveArticleService implements SaveArticle {


    @Autowired
    private AppArticleMapper appArticleMapper;

    @Autowired
    private AppArticleContentMapper appArticleContentMapper;

    @Autowired
    private AppArticleConfigMapper appArticleConfigMapper;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 返回值是文章id
     *
     * @param appArticle
     * @return
     */
    @Override
    public Long saveArticle(AppArticle appArticle) {
        // 保存文章的基本消息
        appArticleMapper.insert(appArticle);
        // 需要生产http文件
        Template template = null;
        try {
            template = configuration.getTemplate("article.ftl");
        }catch (Exception e){
            log.error("未找到模板");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("content", JSONArray.parseArray(appArticle.getContent()));
        StringWriter writer = new StringWriter();
        try {
            template.process(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析错误");
        }
        InputStream is = new ByteArrayInputStream(writer.toString().getBytes());
        String path = fileStorageService.uploadHtmlFile("", appArticle.getId() + ".html", is);
        appArticle.setStaticUrl(path);
        appArticleMapper.update(appArticle,Wrappers.<AppArticle>lambdaUpdate().eq(AppArticle::getId,appArticle.getId()));
        // 保存内容与关系表
        AppArticleContent appArticleContent = new AppArticleContent();
        appArticleContent.setArticleId(appArticle.getId());
        appArticleContent.setContent(appArticle.getContent());
        appArticleContentMapper.insert(appArticleContent);
        // 保存配置消息
        AppArticleConfig appArticleConfig = new AppArticleConfig(appArticleContent.getArticleId());
        appArticleConfigMapper.insert(appArticleConfig);
        log.info("article保存文章结束");
        return appArticle.getId();
    }

    /**
     * 更新文章
     *
     * @param appArticle
     */
    @Override
    public void updateArticle(AppArticle appArticle) {
        // 更新基本消息
        appArticleMapper.update(appArticle,
            Wrappers.<AppArticle>lambdaUpdate().eq(AppArticle::getId, appArticle.getId()));
        // 跟新内容
        AppArticleContent appArticleContent = new AppArticleContent();
        appArticleContent.setArticleId(appArticle.getId());
        appArticleContent.setContent(appArticle.getContent());
        appArticleContentMapper.update(appArticleContent, Wrappers.<AppArticleContent>lambdaUpdate().eq(
            AppArticleContent::getArticleId, appArticleContent.getArticleId()));

    }
}
