package com.lzx.constants;

/**
 * @author lzx
 */
public class SystemConstants {
    /**
     * article-草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * article-正常
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * category-正常
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";
    /**
     * link-正常
     */
    public static final String LINK_STATUS_NORMAL = "0";
    /**
     * comment-文章评论
     */
    public static final String COMMENT_TYPE_ARTICLE = "0";
    /**
     * comment-友链评论
     */
    public static final String COMMENT_TYPE_LINK = "1";
    /**
     * article-viewCount-redis-key
     */
    public static final String ARTICLE_VIEWCOUNT_REDIS_KEY = "article:viewCount";
    public static final String MENU_STATUS_NORMAL = "0";
    public static final String USER_TYPE_ADMIN = "1";
    public static final Object ROLE_STATUS_NORMAL = "0";
    public static final int ARTICLE_NOTDELETE = 0;
    public static final String QUEUE_QUERY_BY_CONTENT = "queue_query_content";
    public static final String EXCHANGE_QUERY = "exchange_query";
    public static final String ROUTINGKEY_QUERY_BY_CONTENT = "query.content";
}
