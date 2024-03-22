package com.strind.controller;

import com.strind.model.article.dtos.ArticleInfoDto;
import com.strind.model.common.RespResult;
import com.strind.service.AppArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/22 9:00
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleInfoController {

    @Autowired
    private AppArticleService appArticleService;

    @PostMapping("/load_article_behavior")
    public RespResult loadArticleInfo(@RequestBody ArticleInfoDto dto){
        return appArticleService.loadArticleInfo(dto);
    }


}
