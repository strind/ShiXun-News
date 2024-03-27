package com.strind.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 13:12
 */
@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    void saveRelation(List<Integer> ids, Integer newsId, Short type);

}
