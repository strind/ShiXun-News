package com.strind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.user.pojos.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/20 22:13
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

    String getName(Integer id);

}
