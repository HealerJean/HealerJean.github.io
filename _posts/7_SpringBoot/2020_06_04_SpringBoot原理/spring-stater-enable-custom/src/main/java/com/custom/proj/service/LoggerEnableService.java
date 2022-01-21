package com.custom.proj.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:45 下午.
 * @description
 */
@Slf4j
public class LoggerEnableService {
    public void saveLog(String logMsg){
        log.info("[CounterEnableService#saveLog] logMsg:{}", logMsg);
    }
}
