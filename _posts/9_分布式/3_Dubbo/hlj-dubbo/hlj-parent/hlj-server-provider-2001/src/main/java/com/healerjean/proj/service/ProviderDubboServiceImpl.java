package com.healerjean.proj.service;


import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// version 服务版本(默认 0.0.0)，建议使用两位数字版本，如：1.0，通常在接口不兼容时版本号才需要升级
// group 服务分组，当一个接口有多个实现，可以用分组区分
@Service(version = "0.1")
public class ProviderDubboServiceImpl implements ProviderDubboService {

    @Override
    public String connect(String name) {
        log.info("消费者：ProviderDubboServiceImpl 【{} 】连接成功", name);
        int i = 1 / 0;
        return name + "：连接成功";
    }

}
