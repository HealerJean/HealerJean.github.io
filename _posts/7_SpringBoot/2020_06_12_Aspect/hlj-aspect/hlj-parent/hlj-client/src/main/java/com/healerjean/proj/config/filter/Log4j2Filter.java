package com.healerjean.proj.config.filter;


import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName Log4j2Filter
 * @date 2020/6/15  20:12.
 * @Description
 */
public class Log4j2Filter implements Filter {

    private static final String REQ_UID = "REQ_UID";
    private FilterConfig filterConfig;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.put(REQ_UID, UUID.randomUUID().toString().replace("-", ""));
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.remove(REQ_UID);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }



}


