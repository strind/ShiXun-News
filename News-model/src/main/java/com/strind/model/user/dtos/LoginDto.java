package com.strind.model.user.dtos;

import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description 用户登录参数
 * @date 2024/3/21 11:36
 */
@Data
public class LoginDto {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

}
