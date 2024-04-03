package com.strind.wemedia.controller;

import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmNewsDto;
import com.strind.model.wemedia.dtos.WmNewsPageDto;
import com.strind.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author strind
 * @version 1.0
 * @description 内容管理
 * @date 2024/3/23 10:15
 */
@RestController
@RequestMapping("/api/v1/news")
@Slf4j
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    public RespResult list(@RequestBody WmNewsPageDto dto){
       return wmNewsService.findList(dto);
    }

    @PostMapping("/submit")
    public RespResult submit(@RequestBody WmNewsDto dto){
        log.info("开始处理提交文章");
        return wmNewsService.submit(dto);
    }

    @GetMapping("/del_news/{id}")
    public RespResult delNews(@PathVariable Integer id){
        return wmNewsService.delNewsById(id);
    }

    @GetMapping("/one/{id}")
    public RespResult one(@PathVariable Integer id){
        return wmNewsService.getNews(id);
    }

    @PostMapping("/down_or_up")
    public RespResult downOrUp(@RequestBody WmNewsDto dto){
        return wmNewsService.downOrUp(dto);
    }

}
