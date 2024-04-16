package com.strind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.strind.constants.UserBehaviorConstants;
import com.strind.exception.CustomException;
import com.strind.mapper.AppUserFanMapper;
import com.strind.mapper.AppUserFollowMapper;
import com.strind.mapper.AppUserMapper;
import com.strind.model.common.RespResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.model.user.dtos.UserRelationDto;
import com.strind.model.user.pojos.AppUser;
import com.strind.model.user.pojos.AppUserFan;
import com.strind.model.user.pojos.AppUserFollow;
import com.strind.service.AppUserRelationService;
import com.strind.thread.AppThreadLocalUtil;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/21 20:33
 */
@Service
public class AppUserRelationServiceImpl implements AppUserRelationService {

    @Autowired
    private AppUserFollowMapper appUserFollowMapper;

    @Autowired
    private AppUserFanMapper appUserFanMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    @Transactional
    public RespResult follow(UserRelationDto dto) {
        AppUser currentUser = AppThreadLocalUtil.getUser();
        if (currentUser == null){
            return RespResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        checkAvailable(dto);
        String authorName = appUserMapper.getName(dto.getAuthorId());
        if (dto.getOperation().equals(UserBehaviorConstants.FOLLOW)){
            // 关注
            AppUserFollow appUserFollow = AppUserFollow.builder()
                .UserId(currentUser.getId())
                .followId(dto.getAuthorId())
                .followName(authorName)
                .level(UserBehaviorConstants.LITTLE)
                .isNotice(UserBehaviorConstants.NON_NOTICE)
                .createdTime(new Date())
                .build();
            // 插入关注表
            appUserFollowMapper.insert(appUserFollow);

            AppUserFan fan = AppUserFan.builder()
                .UserId(dto.getAuthorId())
                .fansId(currentUser.getId())
                .fansName(appUserMapper.getName(currentUser.getId()))
                .level(UserBehaviorConstants.SELDOM) // 忠实度默认正常
                .isDisplay(UserBehaviorConstants.DISPLAY)
                .idShieldLetter(UserBehaviorConstants.NON_SHIELD_LETTER)
                .isShieldComment(UserBehaviorConstants.NON_SHIELD_COMMENT)
                .createdTime(new Date()).build();
            // 插入粉丝表
            appUserFanMapper.insert(fan);
        }else {
            // 取关
            Integer currentUserId = currentUser.getId();
            Integer authorId = dto.getAuthorId();
            // 先删除关注表
            appUserFollowMapper.delete(Wrappers.<AppUserFollow>lambdaQuery()
                .eq(AppUserFollow::getUserId,currentUserId).eq(AppUserFollow::getFollowId,authorId));

            // 在删除粉丝表
            appUserFanMapper.delete(Wrappers.<AppUserFan>lambdaQuery()
                .eq(AppUserFan::getUserId,authorId).eq(AppUserFan::getFansId,currentUserId));
        }

        return RespResult.okResult(200);
    }

    private void checkAvailable(UserRelationDto dto) {
        if (dto.getArticleId() == null || dto.getAuthorId() == null
        || dto.getOperation() < 0 || dto.getOperation() > 1){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
    }
}
