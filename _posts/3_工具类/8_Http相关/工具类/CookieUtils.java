package com.duodian.youhui.admin.utils.cookie;

import com.duodian.youhui.admin.utils.RequestHolder;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Desc: Cookie 工具类
 * @Date:  2018/9/12 下午1:02.
 */

public class CookieHelper {

    // 默认缓存时间,单位/秒, 2H
    private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
    // 保存路径,根路径
    private static final String COOKIE_PATH = "/";
    
    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param ifRemember 
     */
    public static void set(HttpServletResponse response, String key, String value, boolean ifRemember) {
        int age = ifRemember?COOKIE_MAX_AGE:-1;
        set(response, key, value, null, COOKIE_PATH, age, true);
    }

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param maxAge
     */
    private static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }
    
    /**
     * 查询value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 查询Cookie
     *
     * @param request
     * @param key
     */
    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arr_cookie = request.getCookies();
        if (arr_cookie != null && arr_cookie.length > 0) {
            for (Cookie cookie : arr_cookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    /**
     * 删除Cookie
     *
     * @param request
     * @param response
     * @param key
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }


    public static void setCookie(String key,String value,Integer seconds){
        HttpServletResponse response = RequestHolder.getResponse();
        Cookie add = new Cookie(key,value);
        add.setMaxAge(seconds);
        add.setPath("/");
        response.addCookie(add);
    }

    public static String getCookieValue(String key){
        HttpServletRequest request = RequestHolder.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return null;
        }
        for (Cookie cookie : cookies){
            if (StringUtils.equals(cookie.getName(), key)){
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void clearCookie(String key){
        HttpServletResponse response = RequestHolder.getResponse();
        Cookie clear = new Cookie(key,"");
        clear.setMaxAge(0);
        clear.setComment("清除cookie");
        clear.setPath("/");
        response.addCookie(clear);
    }


}
