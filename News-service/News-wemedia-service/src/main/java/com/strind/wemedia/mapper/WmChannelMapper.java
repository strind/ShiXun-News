package com.strind.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.wemedia.pojos.WmChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:16
 */
@Mapper
public interface WmChannelMapper extends BaseMapper<WmChannel> {

    String selectOnlyName(Integer channelId);
}
