package com.strind.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/22 16:31
 */
@Mapper
public interface WmUserMapper extends BaseMapper<WmUser> {

    String selectOnlyName(Integer userId);
}
