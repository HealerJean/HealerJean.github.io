package com.hlj.proj.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SpaceParamsFilter
 * @Date 2019/9/29  14:33.
 * @Description
 */
public class Filter_1 implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        System.out.println("############ Filter_1 在DispatcherServlet之前执行############");

        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("############ Filter_1 在视图页面返回给客户端之前执行，但是执行顺序在Interceptor之后############");
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
