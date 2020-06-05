package com.healerjean.proj.bean;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName AppBean
 * @date 2020/6/4  17:44.
 * @Description
 */
@Slf4j
@Data
public class AppBean {

    private DataBean dataBean;

    public void method() {
       log.info("AppBean--------调用方法：method--------");
    }

}
