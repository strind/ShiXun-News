package com.strind.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.pojos.WmChannel;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:16
 */

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 获取所以的频道
     * @return
     */
    RespResult getChannels();

}
