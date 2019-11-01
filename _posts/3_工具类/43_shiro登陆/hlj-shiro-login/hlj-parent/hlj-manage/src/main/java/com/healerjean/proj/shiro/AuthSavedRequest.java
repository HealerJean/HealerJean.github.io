package com.healerjean.proj.shiro;



import com.healerjean.proj.util.IpUtils;
import com.healerjean.proj.util.UrlUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AuthSavedRequest
 * @Author HealerJean
 * @Date 2019/1/30 13:39
 * @Description 保存请求路径
 */
public class AuthSavedRequest {

    private String method;
    private String queryString;
    private String requestURI;
    private String requestURL;
    private String ip;

    public AuthSavedRequest() {
    }

    public AuthSavedRequest(HttpServletRequest request) {
        this.method = request.getMethod();
        this.queryString = request.getQueryString();
        this.requestURI = request.getRequestURI();
        this.requestURL = UrlUtils.getRealRequestURL(request).toString();
        this.ip = IpUtils.getIp(request);
    }

    public String getMethod() {
        return method;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRequestUrl() {
        StringBuilder requestUrl = new StringBuilder(getRequestURI());
        if (getQueryString() != null) {
            requestUrl.append("?").append(getQueryString());
        }
        return requestUrl.toString();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
