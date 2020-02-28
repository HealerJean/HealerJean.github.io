package com.hlj.proj.config.filter;


import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


public class ReuqestFiterHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    /**
     * 预先出初始化数据
     */
    public ReuqestFiterHttpServletRequestWrapper(HttpServletRequest servletRequest) throws IOException {
        super(servletRequest);
        InputStream inputStream = servletRequest.getInputStream();
        body = IOUtils.toByteArray(inputStream);
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


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
