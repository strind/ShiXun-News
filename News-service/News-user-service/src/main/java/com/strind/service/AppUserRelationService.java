package com.strind.service;


import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.UserRelationDto;

/**
 * @author strind
 * @version 1.0
 * @description 用户关联
 * @date 2024/3/21 20:32
 */
public interface AppUserRelationService {

    /**
     * 用户关注与取关
     * @param dto
     * @return
     */
    RespResult follow(UserRelationDto dto);


}
