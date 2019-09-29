package com.hlj.proj.config.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SpaceHttpServletRequestWrapper
 * @Date 2019/9/29  14:44.
 * @Description
 */
public class SpaceHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public SpaceHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
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
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
