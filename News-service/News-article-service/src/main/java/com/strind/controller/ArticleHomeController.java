package com.strind.controller;

import com.strind.constants.ArticleConstants;
import com.strind.model.article.dtos.ArticleHomeDto;
import com.strind.model.common.RespResult;
import com.strind.service.AppArticleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description 主页控制器
 * @date 2024/3/21 21:03
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Autowired
    private AppArticleService appArticleService;

    /**
     * 加载首页信息
     * @param dto 请求参数
     * @return 文章信息
     */
    @PostMapping("/load")
    public RespResult load(@RequestBody ArticleHomeDto dto){
        // TODO: 2024/3/21 加载首页文章，未使用缓存
        return appArticleService.load(dto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载更多
     * @param dto 请求参数
     * @return 文章信息
     */
    @PostMapping("/loadmore")
    public RespResult loadmore(@RequestBody ArticleHomeDto dto){
        // TODO: 2024/3/21 加载跟多文章
        return  appArticleService.loadmore(dto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载最新
     * @param dto 请求参数
     * @return 文章
     */
    @PostMapping("/loadnew")
    public RespResult loadnew(@RequestBody ArticleHomeDto dto){
        return appArticleService.loadnew(dto,ArticleConstants.LOADTYPE_LOAD_NEW);
    }

}
