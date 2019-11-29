package com.hlj.proj.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HealerJean
 * @ClassName RequestFilter
 * @date 2019-11-25  21:48.
 * @Description
 */
@Slf4j
public class RequestFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        filterChain.doFilter(new ReuqestFiterHttpServletRequestWrapper(request), servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
