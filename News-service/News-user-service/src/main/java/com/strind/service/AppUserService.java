package com.strind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.LoginDto;
import com.strind.model.user.pojos.AppUser;

/**
 * @author strind
 * @version 1.0
 * @description app 端用户服务
 * @date 2024/3/20 22:14
 */
public interface AppUserService extends IService<AppUser> {

    /**
     * 用户登录接口
     * @param dto
     * @return
     */
    RespResult login(LoginDto dto);

}
