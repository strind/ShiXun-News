package com.strind.wemedia.controller;

import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmMaterialDto;
import com.strind.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author strind
 * @version 1.0
 * @description 素材管理
 * @date 2024/3/23 10:15
 */
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    @PostMapping("/list")
    public RespResult list(@RequestBody WmMaterialDto dto){
        return wmMaterialService.findList(dto);
    }

    @PostMapping("/upload_picture")
    public RespResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @GetMapping("/del_picture/{id}")
    public RespResult delPicture(@PathVariable("id") Integer id){
        return wmMaterialService.deleteById(id);
    }

    @GetMapping("/collect/{id}")
    public RespResult collect(@PathVariable("id") Integer id){
        return wmMaterialService.update(id);
    }



}
