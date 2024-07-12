package org.senju.eshopeule.constant.pattern;

public final class RoutePattern {
    public static final String PUBLIC_PREFIX = "/api/p";
    public static final String PRIVATE_PREFIX = "/api/r";

    public static final String PUBLIC_API = PUBLIC_PREFIX + "/**";
    public static final String ROLE_API = PRIVATE_PREFIX + "/*/role/**";
    public static final String STAFF_API = PRIVATE_PREFIX + "/*/staff/**";
    public static final String PROD_IMG_API = PRIVATE_PREFIX + "/*/img/**";
    public static final String PROD_ATTR_API = PRIVATE_PREFIX + "/*/pa/**";
    public static final String PROD_META_API = PRIVATE_PREFIX + "/*/pm/**";
    public static final String PROD_OPTION_API = PRIVATE_PREFIX + "/*/po/**";
    public static final String CATEGORY_API = PRIVATE_PREFIX + "/*/category/**";
    public static final String BRAND_API = PRIVATE_PREFIX + "/*/brand/**";
    public static final String PRODUCT_API = PRIVATE_PREFIX + "/*/prod/**";
    public static final String CART_API = PRIVATE_PREFIX + "/*/cart/**";
}
