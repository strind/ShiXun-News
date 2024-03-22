package com.strind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.common.AppJwtUtil;
import com.strind.mapper.AppUserMapper;
import com.strind.model.common.RespResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.model.user.dtos.LoginDto;
import com.strind.model.user.pojos.AppUser;
import com.strind.service.AppUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/20 22:14
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper,AppUser> implements AppUserService {

    private static final String EMPTY = "";


    /**
     * 用户登录
     * @param dto
     * @return
     */
    @Override
    public RespResult login(LoginDto dto) {
        //1.正常登录 用户名和密码
        if(StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())){
            //1.1 根据手机号查询用户信息
            AppUser dbUser = getOne(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getPhone, dto.getPhone()));
            if(dbUser == null){
                return RespResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }

            //1.2 比对密码
            String salt = dbUser.getSalt();
            String password = dto.getPassword();
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if(!pswd.equals(dbUser.getPassword())){
                return RespResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            //1.3 返回数据  jwt  user
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dbUser.setSalt(EMPTY);
            dbUser.setPassword(EMPTY);
            map.put("user",dbUser);

            return RespResult.okResult(map);
        }else {
            //2.游客登录
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return RespResult.okResult(map);
        }


    }
}
