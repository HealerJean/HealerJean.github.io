package com.hlj.proj.config.interceptor;

import com.hlj.proj.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        log.info("admin:请求地址:[{}];访问ip:[{}]", httpServletRequest.getRequestURL(), IpUtil.getIp());
        log.info(httpServletRequest.getHeader("referer"));
        return true;
    }


}
