package com.strind.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmLoginDto;
import com.strind.model.wemedia.pojos.WmUser;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/22 16:29
 */

public interface WmUserService extends IService<WmUser> {

    /**
     * 用户登录
     * @param dto 参数
     * @return
     */
    RespResult login(WmLoginDto dto);
}
