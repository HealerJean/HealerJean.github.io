/*
 * Copyright (C) 2018 dy_only, Inc. All Rights Reserved.
 */
package com.healerjean.proj.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties properties = new Properties();

    public static String getProperty(String key) {
        return properties.getProperty(key) == null ? "" : properties.get(key).toString();
    }

    static {
        String[] props = new String[]{"systemAuthToken.properties"};
        for (String prop : props) {
            InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(prop);
            if (inputStream != null) {
                Properties propertiest = new Properties();
                try {
                    propertiest.load(inputStream);
                    properties.putAll(propertiest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
