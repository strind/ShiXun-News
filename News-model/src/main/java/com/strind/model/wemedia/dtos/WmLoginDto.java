package com.strind.model.wemedia.dtos;

import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description 用户登录
 * @date 2024/3/22 16:23
 */
@Data
public class WmLoginDto {

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}
