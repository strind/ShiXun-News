package com.strind.model.user.dtos;

import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description 用户关注与取关dto
 * @date 2024/3/21 20:36
 */
@Data
public class UserRelationDto {

    // 文章作者id
    Integer authorId;

    // 文章id
    Long articleId;

    /**
     * 操作；
     * 0 关注
     * 1 取关
     */
    Short operation;
}
