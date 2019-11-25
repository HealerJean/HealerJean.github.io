package com.hlj.proj.config.filter;


import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SpaceParameHttpServletRequestWrapper
 * @Date 2019/9/29  14:44.
 * @Description
 */
public class ReuqestFiterHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    /**
     * 预先出初始化数据
     */
    public ReuqestFiterHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
        super(servletRequest);

        InputStream inputStream = servletRequest.getInputStream();
        body = IOUtils.toByteArray(inputStream);

        BufferedReader reader = servletRequest.getReader();
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        body = sb.toString().getBytes("utf-8");


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




}
