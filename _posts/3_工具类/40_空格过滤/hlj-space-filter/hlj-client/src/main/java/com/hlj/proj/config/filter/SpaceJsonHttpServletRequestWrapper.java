// package com.hlj.proj.config.filter;
//
//
// import com.alibaba.fastjson.JSONArray;
// import com.alibaba.fastjson.JSONObject;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.hlj.proj.dto.Demo.DemoDTO;
// import com.hlj.proj.utils.JsonUtils;
// import org.apache.commons.io.IOUtils;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
//
// import javax.servlet.ReadListener;
// import javax.servlet.ServletInputStream;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletRequestWrapper;
// import java.io.ByteArrayInputStream;
// import java.io.IOException;
// import java.util.*;
//
// /**
//  * @author HealerJean
//  * @version 1.0v
//  * @ClassName SpaceParameHttpServletRequestWrapper
//  * @Date 2019/9/29  14:44.
//  * @Description
//  */
// public class SpaceJsonHttpServletRequestWrapper extends HttpServletRequestWrapper {
//
//     public SpaceJsonHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
//         super(servletRequest);
//     }
//
//     /**
//      * 重写getInputStream方法  Json类型的请求参数必须通过流才能获取到值
//      */
//     @Override
//     public ServletInputStream getInputStream() throws IOException {
//         //非json类型，直接返回
//         if (!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
//             return super.getInputStream();
//         }
//         //为空，直接返回
//         String json = IOUtils.toString(super.getInputStream(), "utf-8");
//         if (StringUtils.isBlank(json)) {
//             return super.getInputStream();
//         }
//         json = JsonUtils.toJsonString(trimSpace(json));
//         ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
//         return new ServletInputStream() {
//             @Override
//             public int read() {
//                 return bis.read();
//             }
//
//             @Override
//             public boolean isFinished() {
//                 return false;
//             }
//
//             @Override
//             public boolean isReady() {
//                 return false;
//             }
//
//             @Override
//             public void setReadListener(ReadListener readListener) {
//             }
//
//         };
//     }
//
//     public static Map<String, Object> trimSpace(String jsonString) {
//         Map<String, Object> map = new HashMap<>();
//         JSONObject jsonObject = JSONObject.parseObject(jsonString);
//         for (Object k : jsonObject.keySet()) {
//             Object o = jsonObject.get(k);
//             if (o instanceof JSONArray) {
//                 List<Object> list = new ArrayList<>();
//                 Iterator<Object> it = ((JSONArray) o).iterator();
//                 while (it.hasNext()) {
//                     Object obj = it.next();
//                     if (obj instanceof JSONObject) {
//                         list.add(trimSpace(obj.toString()));
//                     } else {
//                         list.add(obj.toString().trim());
//                     }
//                 }
//                 map.put(k.toString(), list);
//             } else if (o instanceof JSONObject) {
//                 // 如果内层是json对象的话，继续解析
//                 map.put(k.toString(), trimSpace(o.toString()));
//             } else {
//                 // 如果内层是普通对象的话，直接放入map中
//                 map.put(k.toString(), o.toString().trim());
//             }
//         }
//         return map;
//     }
// }
