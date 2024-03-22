package com.strind.model.article.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @author strind
 * @version 1.0
 * @description app端 文章首页加载dto
 * @date 2024/3/21 21:08
 */
@Data
public class ArticleHomeDto {

    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 分页
    Integer size;
    // 频道 ID
    String tag;
}
