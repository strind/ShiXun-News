package com.strind.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmNewsDto;
import com.strind.model.wemedia.dtos.WmNewsPageDto;
import com.strind.model.wemedia.pojos.WmNews;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:31
 */
public interface WmNewsService extends IService<WmNews> {

    /**
     * 查询内容列表
     * @param dto
     * @return
     */
    RespResult findList(WmNewsPageDto dto);

    /**
     * 提交或保存为草稿
     * @param dto
     * @return
     */
    RespResult submit(WmNewsDto dto);

    /**
     * 根据id获取文章
     * @param id
     * @return
     */
    RespResult getNews(Integer id);

    /**
     * 删除文章（仅自媒体端）
     * @param id
     * @return
     */
    RespResult delNewsById(Integer id);

    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    RespResult downOrUp(WmNewsDto dto);
}
