package com.hlj.proj.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HealerJean
 * @ClassName Interceptor_1
 * @date 2019/11/21  22:08.
 * @Description
 */
@Component
@Slf4j
public class Interceptor_2 implements HandlerInterceptor {

    /**
     * 在DispatcherServlet之前执行
     * */
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        System.out.println("************ Interceptor_2  在DispatcherServlet之前执行**********");
        return true;
    }

    /**
     * 在controller执行之后的DispatcherServlet之后执行
     * */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
        System.out.println("************ Interceptor_2  在controller执行之后的DispatcherServlet之后执行**********");
    }

    /**
     * 在页面渲染完成返回给客户端之前执行
     * */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        System.out.println("************ Interceptor_2  在页面渲染完成返回给客户端之前执行**********");
    }
}
