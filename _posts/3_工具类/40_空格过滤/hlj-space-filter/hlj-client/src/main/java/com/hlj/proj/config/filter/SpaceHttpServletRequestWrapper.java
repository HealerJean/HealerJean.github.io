package com.hlj.proj.config.filter;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hlj.proj.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SpaceParameHttpServletRequestWrapper
 * @Date 2019/9/29  14:44.
 * @Description
 */
public class SpaceHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    /**
     * 预先出初始化数据
     */
    public SpaceHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
        super(servletRequest);
        BufferedReader reader = servletRequest.getReader();
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        if (StringUtils.isNotBlank(json)) {
            json = JsonUtils.toJsonString(trimSpace(json));
        }
        body = json.getBytes("utf-8");
    }


    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return new String[0];
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = values[i].trim();
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 重写getInputStream方法  Json类型的请求参数必须通过流才能获取到值
     */
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bis.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

        };
    }


    public static Map<String, Object> trimSpace(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        for (Object k : jsonObject.keySet()) {
            Object o = jsonObject.get(k);
            if (o instanceof JSONArray) {
                List<Object> list = new ArrayList<>();
                Iterator<Object> it = ((JSONArray) o).iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (obj instanceof JSONObject) {
                        list.add(trimSpace(obj.toString()));
                    } else {
                        list.add(obj.toString().trim());
                    }
                }
                map.put(k.toString(), list);
            } else if (o instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), trimSpace(o.toString()));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                map.put(k.toString(), o.toString().trim());
            }
        }
        return map;
    }



}
