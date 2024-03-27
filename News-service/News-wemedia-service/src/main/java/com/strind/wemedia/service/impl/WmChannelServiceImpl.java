package com.strind.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.pojos.WmChannel;
import com.strind.wemedia.mapper.WmChannelMapper;
import com.strind.wemedia.service.WmChannelService;
import org.springframework.stereotype.Service;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:17
 */
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    /**
     * 获取所以的频道
     * @return
     */
    @Override
    public RespResult getChannels() {
        return RespResult.okResult(list());
    }
}
