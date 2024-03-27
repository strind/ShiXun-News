package com.strind.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.common.AppJwtUtil;
import com.strind.model.common.RespResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.model.wemedia.dtos.WmLoginDto;
import com.strind.model.wemedia.pojos.WmUser;
import com.strind.wemedia.mapper.WmUserMapper;
import com.strind.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/22 16:31
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    /**
     * 用户登录
     *
     * @param dto 参数
     * @return
     */
    @Override
    public RespResult login(WmLoginDto dto) {
        //1.检查参数
        if(StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"用户名或密码为空");
        }

        //2.查询用户
        WmUser wmUser = getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, dto.getName()));
        if(wmUser == null){
            return RespResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //3.比对密码
        String salt = wmUser.getSalt() == null ? "" : wmUser.getSalt();
        String pswd = dto.getPassword();
        pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
        if(pswd.equals(wmUser.getPassword())){
            //4.返回数据  jwt
            Map<String,Object> map  = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(wmUser.getId().longValue()));
            wmUser.setSalt("");
            wmUser.setPassword("");
            map.put("user",wmUser);
            return RespResult.okResult(map);

        }else {
            return RespResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }


    }
}
