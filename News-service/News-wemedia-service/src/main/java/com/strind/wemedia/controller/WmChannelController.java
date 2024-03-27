package com.strind.wemedia.controller;

import com.strind.model.common.RespResult;
import com.strind.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author strind
 * @version 1.0
 * @description 自媒体频道
 * @date 2024/3/23 10:14
 */
@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public RespResult getChannels(){
        return wmChannelService.getChannels();
    }

}
