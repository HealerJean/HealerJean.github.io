package com.healerjean.proj.shiro;

/**
 * @ClassName AuthConstants
 * @Author TD
 * @Date 2019/6/3 19:57
 * @Description 权限拦截常量
 */
public class AuthConstants {

    /** 客户端关键字：用户 */
    public static final String AUTH_USER = "auth_user";
    /** 客户端关键字：用户菜单 */
    public static final String AUTH_MENU = "auth_menu";
    /** 客户端关键字：AuthRequest */
    public static final String AUTH_REQUEST = "auth_request";

    /**服务端存储*/
    public static final String SESSION_TYPE_TOKEN = "TOKEN";
    /** 客户端请求给服务端带的header */
    public final static String HEADER_TOKEN_NAME = "token";

}
