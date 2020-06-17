package com.healerjean.proj.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName Log4j2ReqUidFilter
 * @date 2020/6/17  10:13.
 * @Description
 */
public class Log4j2ReqUidFilter implements Filter {

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
