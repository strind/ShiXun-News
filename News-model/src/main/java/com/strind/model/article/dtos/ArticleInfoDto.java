package com.strind.model.article.dtos;

import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description 加载文章详情用dto
 * @date 2024/3/22 9:02
 */
@Data
public class ArticleInfoDto {

    // 文章id
    Long articleId;

    // 设备id
    Integer equipmentId;

    // 作者id
    Integer authorId;
}
