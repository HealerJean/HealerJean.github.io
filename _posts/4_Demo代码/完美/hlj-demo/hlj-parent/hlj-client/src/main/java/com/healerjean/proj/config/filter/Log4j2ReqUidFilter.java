package com.healerjean.proj.config.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * Log4j2ReqUidFilter
 *
 * @author zhangyujin
 * @date 2023/6/15  11:40.
 */
public class Log4j2ReqUidFilter implements Filter {

    private static final String REQ_UID = "REQ_UID";
    private FilterConfig filterConfig;


    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            MDC.put(REQ_UID, UUID.randomUUID().toString().replace("-", ""));
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            MDC.remove(REQ_UID);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }


}

