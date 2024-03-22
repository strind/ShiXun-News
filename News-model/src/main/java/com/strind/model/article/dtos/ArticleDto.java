package com.strind.model.article.dtos;

import com.strind.model.article.pojos.AppArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/20 22:07
 */
@Data
public class ArticleDto extends AppArticle {

    /**
     * 文章内容
     */
    private String content;
}
