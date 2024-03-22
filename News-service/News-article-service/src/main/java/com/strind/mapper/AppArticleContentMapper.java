package com.strind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.article.pojos.AppArticleContent;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author strind
 * @version 1.0
 * @description 文章内容
 * @date 2024/3/21 21:01
 */
@Mapper
public interface AppArticleContentMapper extends BaseMapper<AppArticleContent> {

}
