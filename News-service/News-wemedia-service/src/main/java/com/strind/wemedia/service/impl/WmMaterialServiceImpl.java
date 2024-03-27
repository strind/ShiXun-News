package com.strind.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.constants.WmmediaConstants;
import com.strind.file.service.FileStorageService;
import com.strind.model.common.RespResult;
import com.strind.model.common.dtos.PageResponseResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.model.wemedia.dtos.WmMaterialDto;
import com.strind.model.wemedia.pojos.WmMaterial;
import com.strind.model.wemedia.pojos.WmNewsMaterial;
import com.strind.thread.WmmediaThreadLocalUtil;
import com.strind.wemedia.mapper.WmMaterialMapper;
import com.strind.wemedia.mapper.WmNewsMaterialMapper;
import com.strind.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 12:09
 */
@Service
@Slf4j
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 文章素材关联mapper
     */
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 查询素材
     *
     * @param dto
     * @return
     */
    @Override
    public RespResult findList(WmMaterialDto dto) {
        // 校验参数
        dto.checkParam();

        // 分页查询
        IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        // 封装查询条件
        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper<>();
        if (dto.getIsCollection() == null){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        queryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        queryWrapper.eq(WmMaterial::getUserId, WmmediaThreadLocalUtil.getUser().getId());

        // 开始查询
        page = page(page, queryWrapper);

        RespResult result = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());

        return result;
    }


    /**
     * 上传素材
     *
     * @param multipartFile
     * @return
     */
    @Override
    public RespResult uploadPicture(MultipartFile multipartFile) {
        // 校验参数
        if (multipartFile == null || multipartFile.isEmpty()){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 将图片上传到Minio中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        // abc.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        // 后缀
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;

        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("图片上传到Minio，fileId: {}",fileId);
        }catch (IOException e){
            e.printStackTrace();
            log.error("文件上传失败");
        }

        // 保存到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmmediaThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection(WmmediaConstants.NON_COLLECTION);
        wmMaterial.setType(WmmediaConstants.PICTURE_TYPE);
        wmMaterial.setCreatedTime(new Date());

        save(wmMaterial);

        return RespResult.okResult(wmMaterial);
    }

    /**
     * 删除元素
     *
     * @param id
     * @return
     */
    @Override
    public RespResult deleteById(Integer id) {
        // 参数校验
        if (id == null){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查看是否有文章应用了该素材
        LambdaQueryWrapper<WmNewsMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmNewsMaterial::getMaterialId,id);
        Integer count = wmNewsMaterialMapper.selectCount(queryWrapper);
        if (count > 0){
            return RespResult.errorResult(AppHttpCodeEnum.MATERIAL_BE_USING);
        }

        try {
            removeById(id);
        }catch (Exception e){
            log.error("图片删除异常，图片id ：{}",id);
        }

        return RespResult.okResult("删除成功");
    }

    /**
     * 跟新文章
     *
     * @param id
     * @return
     */
    @Override
    public RespResult update(Integer id) {
        // 参数校验
        if (id == null){
            return RespResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial wmMaterial = getOne(Wrappers.<WmMaterial>lambdaQuery().eq(WmMaterial::getId, id));
        Short isCollection = wmMaterial.getIsCollection();
        if (WmmediaConstants.NON_COLLECTION.equals(isCollection)){
            wmMaterial.setIsCollection(WmmediaConstants.COLLECTION);
        }else wmMaterial.setIsCollection(WmmediaConstants.NON_COLLECTION);

        updateById(wmMaterial);
        return RespResult.okResult("修改成功");
    }


}
