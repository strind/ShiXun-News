package com.strind.controller;

import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.UserRelationDto;
import com.strind.service.AppUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description 用户关注与取关
 * @date 2024/3/20 22:10
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController {

    @Autowired
    private AppUserRelationService appUserRelationService;

    @PostMapping("/user_follow")
    public RespResult follow(@RequestBody UserRelationDto dto){
        return appUserRelationService.follow(dto);
    }



}
