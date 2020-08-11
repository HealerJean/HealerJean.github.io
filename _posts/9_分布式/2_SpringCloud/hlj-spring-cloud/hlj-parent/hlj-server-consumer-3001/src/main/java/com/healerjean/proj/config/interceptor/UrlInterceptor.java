package com.healerjean.proj.config.interceptor;

import com.healerjean.proj.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
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
    @Autowired
    private Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("请求地址:[{}];访问ip:[{}]", httpServletRequest.getRequestURL(), IpUtil.getIp());
        tracer.addTag("operator", "HealerJean");
        log.info("traceId[{}]", tracer.getCurrentSpan().traceIdString());
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
