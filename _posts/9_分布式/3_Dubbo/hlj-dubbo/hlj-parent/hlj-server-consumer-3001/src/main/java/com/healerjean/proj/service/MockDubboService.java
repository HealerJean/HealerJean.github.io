package com.healerjean.proj.service;

/**
 * @author HealerJean
 * @ClassName MokeDubboService
 * @date 2020/6/24  17:04.
 * @Description
 */
public class MockDubboService implements ProviderDubboService {

    @Override
    public String connect(String name) {
        return "mock：" + name;
    }
}
