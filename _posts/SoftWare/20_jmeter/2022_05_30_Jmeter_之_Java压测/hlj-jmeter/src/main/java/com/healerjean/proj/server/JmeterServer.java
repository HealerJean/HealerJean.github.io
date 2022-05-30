package com.healerjean.proj.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangyujin
 * @date 2022/5/30  11:00.
 */

@Slf4j
public class JmeterServer {


    public String yc(){
        for (int i = 0 ; i< 100000; i++){
            try {
                log.info("[JmeterServer#yc] start ");
                Thread.sleep(1000L);
                log.info("[JmeterServer#yc] end ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Thread.currentThread().getName();
    }

    @Test
    public void test(){
        yc();
    }


}
