// package com.hlj.proj.config.filter;
//
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import java.io.IOException;
//
// /**
//  * @author HealerJean
//  * @version 1.0v
//  * @ClassName SpaceParamsFilter
//  * @Date 2019/9/29  14:33.
//  * @Description Josn请求空格过滤
//  */
// public class SpaceJsonFilter implements Filter {
//
//     private FilterConfig filterConfig;
//
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         this.filterConfig = filterConfig;
//     }
//
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
//             IOException, ServletException {
//         filterChain.doFilter(new SpaceJsonHttpServletRequestWrapper(
//                 (HttpServletRequest) servletRequest), servletResponse);
//     }
//
//     @Override
//     public void destroy() {
//         this.filterConfig = null;
//     }
// }
