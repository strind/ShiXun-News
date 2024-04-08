package com.strind.constants;

/**
 * @author strind
 * @version 1.0
 * @description article 常量
 * @date 2024/3/21 21:40
 */
public class ArticleConstants {

    public static final Short LOADTYPE_LOAD_MORE = 1;
    public static final Short LOADTYPE_LOAD_NEW = 2;

    public static final Integer MAX_PAGE_SIZE = 50;
    public static final Integer DEFAULT_PAGE_SIZE = 50;

    public static final String DEFAULT_TAG = "__all__";

    public static final String ARTICLE_ES_SYNC_TOPIC = "article.es.sync.topic";

    public static final Integer HOT_ARTICLE_LIKE_WEIGHT = 3;
    public static final Integer HOT_ARTICLE_COMMENT_WEIGHT = 5;
    public static final Integer HOT_ARTICLE_COLLECTION_WEIGHT = 8;
    public static final String HOT_ARTICLE_FIRST_PAGE = "hot_article_first_page_";

    public static final Short LIKE_TYPE = 0;
    public static final Short COLLECTION_TYPE = 1;
    public static final Short COMMENT_TYPE = 2;
    public static final Short VIEWS_TYPE = 3;


}
