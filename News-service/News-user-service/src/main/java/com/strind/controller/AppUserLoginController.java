package com.strind.controller;

import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.LoginDto;
import com.strind.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description app 端用户登录
 * @date 2024/3/20 22:10
 */
@RestController
@RequestMapping("/api/v1/login")
public class AppUserLoginController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login_auth")
    public RespResult login(@RequestBody LoginDto dto){
        return appUserService.login(dto);
    }

}
