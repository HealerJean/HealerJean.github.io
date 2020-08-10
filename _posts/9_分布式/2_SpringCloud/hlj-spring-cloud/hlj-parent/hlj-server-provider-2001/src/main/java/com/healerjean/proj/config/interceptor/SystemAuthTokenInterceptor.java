package com.healerjean.proj.config.interceptor;

import com.healerjean.proj.util.IpUtil;
import com.healerjean.proj.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class SystemAuthTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o) throws Exception {
        Object authTokenObject = httpServletRequest.getHeader("systemAuthToken");
        if (authTokenObject != null) {
            try {
                String[] authToken = String.valueOf(authTokenObject).split("_");
                if (PropertiesUtil.getProperty("systemAuthToken." + authToken[0]).equals(authToken[1])) {
                    return true;
                }
            } catch (Exception e) {
                log.error("systemAuthToken 认证失败", e);
            }
        }

        log.error("systemAuthToken 认证失败，请求地址:[{}];访问ip:[{}]", httpServletRequest.getRequestURL(), IpUtil.getIp());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print("systemAuthToken 认证失败");
        out.flush();
        out.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
