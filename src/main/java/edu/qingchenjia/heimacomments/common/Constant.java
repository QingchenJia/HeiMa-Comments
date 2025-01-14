package edu.qingchenjia.heimacomments.common;

public class Constant {
    public static final String IMAGE_PATH = "D:\\Code-Storage\\HeiMaComments\\images\\";

    public static final String USER_DEFAULT_NICKNAME = "user_";

    public static final String REDIS_NO_DATA = "";

    public static final Long REDIS_NO_DATA_TTL = 3L;

    public static final String REDIS_LOGIN_CODE_KEY = "login:code:";

    public static final long REDIS_LOGIN_CODE_TTL = 2L;

    public static final String REDIS_LOGIN_TOKEN_KEY = "login:token:";

    public static final long REDIS_LOGIN_TOKEN_TTL = 30L;

    public static final String REDIS_CACHE_SHOP_KEY = "cache:shop:";

    public static final long REDIS_CACHE_SHOP_TTL = 15L;

    public static final String REDIS_CACHE_SHOPTYPES_KEY = "cache:shopTypes:";

    public static final long REDIS_CACHE_SHOPTYPES_TTL = 20L;

    public static final int MAX_PAGE_SIZE = 10;

    public static final int MAX_SCROLL_PAGE_SIZE = 3;

    public static final String REDIS_LIKE_BLOG_KEY = "like:blog:";

    public static final String REDIS_FOLLOW_BLOG_KEY = "follow:blog:user:";

    public static final String REDIS_NEAR_SHOPS_KEY = "near:shop:type:";

    public static final String REDIS_SIGN_USER_KEY = "sign:user:";
}
