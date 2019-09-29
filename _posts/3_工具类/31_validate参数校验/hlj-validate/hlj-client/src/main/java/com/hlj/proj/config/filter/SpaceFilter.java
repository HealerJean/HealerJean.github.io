package com.hlj.proj.config.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SpaceFilter
 * @Date 2019/9/29  14:33.
 * @Description
 */
@WebFilter(urlPatterns = {"/*"})
public class SpaceFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        filterChain.doFilter(new SpaceHttpServletRequestWrapper(request), servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
