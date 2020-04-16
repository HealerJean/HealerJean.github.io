package com.healerjean.proj.config.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author HealerJean
 * @ClassName ZuulTokenFilter
 * @date 2020/4/14  19:40.
 * @Description
 */
@Slf4j
public class ZuulTokenFilter extends ZuulFilter {

    /**
     * 1、过滤器类型 这里定义为pre 意味着在请求路由之前执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 2、过滤器执行顺序，当请求中存在多个过滤器时，需要根据这个返回值来依次执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 3、判断过滤器是否需要被执行，这里返回，true 因此这个过滤器对所有的请求都会生效，
     * 实际操作中可以利用这里指定过滤器的有效范围
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 4、过滤器的具体逻辑
     */
    @Override
    public Object run() {
        RequestContext requestContext  = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext .getRequest();
        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        Object systemAuthToken = request.getHeader("systemAuthToken");
        if (systemAuthToken == null) {
            log.warn("systemAuthToken is empty");
            requestContext .setSendZuulResponse(false);
            requestContext .setResponseStatusCode(401);
            requestContext.setResponseBody("systemAuthToken is empty");
            return null;
        }
        log.info("systemAuthToken is ok");
        return null;
    }

}

