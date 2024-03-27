package com.strind.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.strind.commonInterface.SaveArticle;
import com.strind.mapper.AppArticleConfigMapper;
import com.strind.mapper.AppArticleContentMapper;
import com.strind.mapper.AppArticleMapper;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.article.pojos.AppArticleConfig;
import com.strind.model.article.pojos.AppArticleContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 返回值是文章id
     * @param appArticle
     * @return
     */
    @Override
    public Long saveArticle(AppArticle appArticle) {
        // 保存文章的基本消息
        appArticleMapper.insert(appArticle);
        // 保存内容与关系表
        AppArticleContent appArticleContent = new AppArticleContent();
        appArticleContent.setArticleId(appArticle.getId());
        appArticleContent.setContent(appArticle.getContent());
        appArticleContentMapper.insert(appArticleContent);
        // 保存配置消息
        AppArticleConfig appArticleConfig  = new AppArticleConfig(appArticleContent.getArticleId());
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
        appArticleMapper.update(appArticle,Wrappers.<AppArticle>lambdaUpdate().eq(AppArticle::getId,appArticle.getId()));
        // 跟新内容
        AppArticleContent appArticleContent = new AppArticleContent();
        appArticleContent.setArticleId(appArticle.getId());
        appArticleContent.setContent(appArticle.getContent());
        appArticleContentMapper.update(appArticleContent,Wrappers.<AppArticleContent>lambdaUpdate().eq(
            AppArticleContent::getArticleId, appArticleContent.getArticleId()));

    }
}
