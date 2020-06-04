package com.healerjean.proj.service;


import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProviderDubboServiceImpl implements ProviderDubboService {

    @Override
    public String connect(String name) {
        log.info("消费者：【{} 】连接成功", name);
        return name + "：连接成功";
    }

}
