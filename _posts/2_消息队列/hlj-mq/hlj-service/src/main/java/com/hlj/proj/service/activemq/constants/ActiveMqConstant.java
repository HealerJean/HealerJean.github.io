package com.hlj.proj.service.activemq.constants;

import org.apache.activemq.ActiveMQConnection;

/**
 * @author HealerJean
 * @ClassName ActiveMqConstant
 * @date 2019/8/28  12:13.
 * @Description
 */
public class ActiveMqConstant {

    /** 默认用户名   */
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    /**  * 默认密码 */
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    /**   默认连接地址  */
    public static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
}
