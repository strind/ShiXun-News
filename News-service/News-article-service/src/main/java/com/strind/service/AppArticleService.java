package com.strind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.article.dtos.ArticleCollectionDto;
import com.strind.model.article.dtos.ArticleHomeDto;
import com.strind.model.article.dtos.ArticleInfoDto;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.common.RespResult;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/21 21:05
 */
public interface AppArticleService extends IService<AppArticle> {

    /**
     * 加载首页内容
     *
     * @param dto 参数
     * @param b
     * @return 文章
     */
    RespResult load(ArticleHomeDto dto, Short type, boolean firstPage);

    /**
     * 加载更多
     * @param dto 参数
     * @param type 类型
     * @return 文章
     */
    RespResult loadmore(ArticleHomeDto dto, Short type);

    /**
     * 加载最新文章
     * @param dto 参数
     * @param type 类型
     * @return 文章
     */
    RespResult loadnew(ArticleHomeDto dto, Short type);

    /**
     * 加载文章详情
     * @param dto 参数
     * @return 文章详情
     */
    RespResult loadArticleInfo(ArticleInfoDto dto);

    /**
     * 收藏
     * @param dto
     * @return
     */
    RespResult collection(ArticleCollectionDto dto);
}
