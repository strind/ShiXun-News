package com.strind.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.strind.model.wemedia.pojos.WmNews;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 10:31
 */
@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {

    Long getArticleId(Integer id);

}
