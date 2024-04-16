package com.strind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.article.dtos.ArticleHomeDto;
import com.strind.model.article.pojos.AppArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


/**
 * @author strind
 * @version 1.0
 * @description app 端文章基本信息，无具体内容
 * @date 2024/3/21 21:00
 */
@Mapper
public interface AppArticleMapper extends BaseMapper<AppArticle> {

    List<AppArticle> loadArticleList(ArticleHomeDto dto, Short type);

    List<AppArticle> selectIn7Days(Date time);


}
