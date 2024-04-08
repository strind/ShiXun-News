package com.strind.commonInterface.article;

import com.strind.model.article.pojos.AppArticle;

/**
 * @author strind
 * @version 1.0
 * @description 保存文章的接口
 * @date 2024/3/25 15:38
 */
public interface SaveArticle {

    /**
     * 返回值是文章id
     * @param appArticle
     * @return
     */
    Long saveArticle(AppArticle appArticle);

    /**
     * 更新文章
     * @param appArticle
     */
    void updateArticle(AppArticle appArticle);

}
