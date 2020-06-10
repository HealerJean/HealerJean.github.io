/*
 * Copyright (C) 2018 dy_only, Inc. All Rights Reserved.
 */
package com.healerjean.proj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties properties = null;
    private static String[] props = new String[]{"application-db.properties"};

    public static String getProperty(String key) {
        if (properties == null) {
            initProperty();
        }
        return properties.getProperty(key) == null ? "" : properties.get(key).toString();
    }

    public static synchronized void initProperty() {
        if (properties == null) {
            properties = new Properties();
            for (String prop : props) {
                InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(prop);
                if (inputStream != null) {
                    Properties propertiest = new Properties();
                    try {
                        propertiest.load(inputStream);
                        properties.putAll(propertiest);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        }
    }

}
