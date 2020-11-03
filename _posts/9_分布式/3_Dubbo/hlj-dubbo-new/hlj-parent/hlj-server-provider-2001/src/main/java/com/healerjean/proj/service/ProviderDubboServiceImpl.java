package com.healerjean.proj.service;


import com.healerjean.proj.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(version = "0.1", group = "healerjean")
public class ProviderDubboServiceImpl implements ProviderDubboService {

    @Override
    public UserDTO connect(String name) {
        log.info("消费者：ProviderDubboServiceImpl 【{} 】连接成功", name);
        return  new UserDTO().setName(name).setId(1L).setDescription("连接成功");
    }

}
