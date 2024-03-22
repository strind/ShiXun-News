package com.strind.wemedia.controller;

import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmLoginDto;
import com.strind.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description 自媒体用户登录
 * @date 2024/3/22 16:29
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private WmUserService wmUserService;

    @PostMapping("/in")
    public RespResult login(@RequestBody WmLoginDto dto){
        return wmUserService.login(dto);
    }

}
