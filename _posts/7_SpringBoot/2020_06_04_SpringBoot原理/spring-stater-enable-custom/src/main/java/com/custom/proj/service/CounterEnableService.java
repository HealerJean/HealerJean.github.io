package com.custom.proj.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:45 下午.
 * @description
 */
@Slf4j
public class CounterEnableService {

    public void add(int count ){
        log.info("CounterEnableService#add]  count:{}", count);
    }
}
