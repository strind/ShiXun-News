package com.strind.model.wemedia.dtos;

import com.strind.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 12:12
 */
@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 是否收藏
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
