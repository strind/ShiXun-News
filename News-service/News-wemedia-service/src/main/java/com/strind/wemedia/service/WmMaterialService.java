package com.strind.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.strind.model.common.RespResult;
import com.strind.model.wemedia.dtos.WmMaterialDto;
import com.strind.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 12:09
 */
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 查询素材
     * @param dto
     * @return
     */
    RespResult findList(WmMaterialDto dto);

    /**
     * 上传素材
     * @param multipartFile
     * @return
     */
    RespResult uploadPicture(MultipartFile multipartFile);

    /**
     * 删除元素
     * @param id
     * @return
     */
    RespResult deleteById(Integer id);

    /**
     * 跟新文章
     * @param id
     * @return
     */
    RespResult update(Integer id);
}
