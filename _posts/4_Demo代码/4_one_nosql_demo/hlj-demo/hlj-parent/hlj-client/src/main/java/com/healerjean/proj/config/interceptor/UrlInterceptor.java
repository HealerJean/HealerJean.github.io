package com.healerjean.proj.config.interceptor;

import com.healerjean.proj.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author HealerJean
 * @version 1.0v
 * @Description
 * @ClassName ${NAME}
 * @date ${DATE}  ${TIME}.
 */
@Component
@Slf4j
public class UrlInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // log.info("请求method：{}；请求地址：[{}]；访问ip：[{}]", httpServletRequest.getMethod(), httpServletRequest.getRequestURL(), IpUtils.getIp(httpServletRequest));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
