package com.strind.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.wemedia.pojos.WmSensitive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/25 12:46
 */
@Mapper
public interface WmSensitiveMapper extends BaseMapper<WmSensitive> {
    List<String> getSensitive();
}
