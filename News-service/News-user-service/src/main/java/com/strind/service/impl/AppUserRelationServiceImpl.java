package com.strind.service.impl;

import com.strind.model.common.RespResult;
import com.strind.model.user.dtos.UserRelationDto;
import com.strind.service.AppUserRelationService;
import org.springframework.stereotype.Service;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/21 20:33
 */
@Service
public class AppUserRelationServiceImpl implements AppUserRelationService {

    @Override
    public RespResult follow(UserRelationDto dto) {
        // TODO: 2024/3/21 用户关注与取关功能
        return null;
    }
}
