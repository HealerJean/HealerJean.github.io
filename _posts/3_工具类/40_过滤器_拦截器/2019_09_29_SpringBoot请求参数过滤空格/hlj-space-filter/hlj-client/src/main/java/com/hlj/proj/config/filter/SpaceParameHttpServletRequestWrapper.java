// package com.hlj.proj.config.filter;
//
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletRequestWrapper;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map;
// import java.util.Set;
//
// /**
//  * @author HealerJean
//  * @version 1.0v
//  * @ClassName SpaceParameHttpServletRequestWrapper
//  * @Date 2019/9/29  14:44.
//  * 空格过滤处理
//  * 下面的将构造器中将所有的请求的参数获取了（包括404），但是其实并没有进行空格过滤处理），
//  * 一般普通请求空格过滤用下面的就可以了
//  */
// public class SpaceParameHttpServletRequestWrapper extends HttpServletRequestWrapper {
//
//     /**
//      * 下面的将构造器中将所有的请求的参数获取了（包括404），但是其实并没有进行空格过滤处理），
//      * 一般普通请求空格过滤用下面的就可以了
//      */
//     public SpaceParameHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
//         super(servletRequest);
//     }
//     @Override
//     public String[] getParameterValues(String parameter) {
//         String[] values = super.getParameterValues(parameter);
//         if (values == null) {
//             return new String[0];
//         }
//         int count = values.length;
//         String[] encodedValues = new String[count];
//         for (int i = 0; i < count; i++) {
//             encodedValues[i] = values[i].trim();
//         }
//         return encodedValues;
//     }
//
//     @Override
//     public String getParameter(String name) {
//         String value = super.getParameter(name);
//         if (value == null) {
//             return null;
//         }
//         return value.trim();
//     }
//
//
//
//     // private Map<String, String[]> params = new HashMap<>();
//     //
//     // public SpaceParameHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
//     //     super(servletRequest);
//     //
//     //     Map<String, String[]> requestMap = servletRequest.getParameterMap();
//     //     this.params.putAll(requestMap);
//     //     this.modifyParameterValues();
//     // }
//     //
//     // /**
//     //  *   404是不会进入的
//     //  *  重写getParameterValues方法，通过循环取出每一个请求结果，再对请求结果进行过滤
//     //  */
//     // @Override
//     // public String[] getParameterValues(String name) {
//     //     return params.get(name);
//     // }
//     //
//     // /**
//     //  * 重写getParameter方法 对请求结果进行过滤
//     //  **/
//     // @Override
//     // public String getParameter(String name) {
//     //     String[]values = params.get(name);
//     //     if (values == null || values.length == 0) {
//     //         return null;
//     //     }
//     //     return values[0];
//     // }
//     // public void modifyParameterValues() {
//     //     Set<String> set = params.keySet();
//     //     Iterator<String> it = set.iterator();
//     //     while (it.hasNext()) {
//     //         String key = it.next();
//     //         String[] values = params.get(key);
//     //         values[0] = values[0].trim();
//     //         params.put(key, values);
//     //     }
//     // }
//
//
// }
