package com.strind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strind.constants.ArticleConstants;
import com.strind.mapper.AppArticleMapper;
import com.strind.model.article.dtos.ArticleHomeDto;
import com.strind.model.article.dtos.ArticleInfoDto;
import com.strind.model.article.pojos.AppArticle;
import com.strind.model.common.RespResult;
import com.strind.model.common.enums.AppHttpCodeEnum;
import com.strind.service.AppArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author strind
 * @version 1.0
 * @description 文章首页服务
 * @date 2024/3/21 21:06
 */

@Service
public class AppArticleServiceImpl extends ServiceImpl<AppArticleMapper, AppArticle> implements AppArticleService {

    @Autowired
    private AppArticleMapper appArticleMapper;

    @Override
    public RespResult load(ArticleHomeDto dto, Short type) {
        checkOrInit(dto, type);

        List<AppArticle> appArticles = appArticleMapper.loadArticleList(dto,type);
        return RespResult.okResult(appArticles);
    }


    @Override
    public RespResult loadmore(ArticleHomeDto dto, Short type) {
        return load(dto,type);
    }

    /**
     * @param dto  参数
     * @param type 类型
     * @return
     */
    @Override
    public RespResult loadnew(ArticleHomeDto dto, Short type) {
        return load(dto,type);
    }

    /**
     * @param dto 参数
     * @return
     */
    @Override
    public RespResult loadArticleInfo(ArticleInfoDto dto) {
        return null;
    }

    /**
     * 参数校验函数
     * @param dto
     * @param type
     */
    private static void checkOrInit(ArticleHomeDto dto, Short type) {
        if (Objects.isNull(dto)){
            RespResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 开始参数校验
        Integer size = dto.getSize();
        if (size <= 0 || size > ArticleConstants.MAX_PAGE_SIZE){
            dto.setSize(ArticleConstants.DEFAULT_PAGE_SIZE);
        }
        if (type == null || (!ArticleConstants.LOADTYPE_LOAD_MORE.equals(type) && !ArticleConstants.LOADTYPE_LOAD_NEW.equals(
            type))){
            // 默认加载更多
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        if (dto.getTag() == null){
            // 默认是首页
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
    }

}
