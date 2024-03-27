package com.strind.model.wemedia.dtos;

import com.strind.model.common.dtos.PageRequestDto;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * @author strind
 * @version 1.0
 * @description 自媒体 内容请求封装
 * @date 2024/3/23 10:34
 */
@Data
public class WmNewsPageDto extends PageRequestDto {

    /**
     * 状态
     */
    private Short status;
    /**
     * 开始时间
     */
    private Date beginPubDate;
    /**
     * 结束时间
     */
    private Date endPubDate;
    /**
     * 所属频道ID
     */
    private Integer channelId;
    /**
     * 关键字
     */
    private String keyword;

}
