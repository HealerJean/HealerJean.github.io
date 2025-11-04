package com.healerjean.proj.utils.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HttpUtils
 *
 * @author zhangyujin
 * @date 2025/3/17
 */
@Slf4j
public class HttpUtils {


    /**
     * 返回写入Response
     *
     * @param obj      需要输出的对象
     * @param response HttpServletResponse
     */
    public static void writeHttpResponse(Object obj, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("WebUtils#writeRes:{}", JSONObject.toJSONString(obj));
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            if (obj instanceof String) {
                writer.write(obj.toString());
            } else {
                writer.write(JSONObject.toJSONString(obj));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
