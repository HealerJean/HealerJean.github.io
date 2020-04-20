package com.hlj.proj.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HealerJean
 * @ClassName XssFilter
 * @Date 2019/11/21  17:38.
 * @Description 防Xss攻击Filter ，只能阻止普通的http请求，不能阻止Json请求，如果需要组织Json请求则需要按照空格过滤器一样进行配置
 */
public class XssFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        filterChain.doFilter(new XssHttpServletRequestWrapper(
                (HttpServletRequest) servletRequest), servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
