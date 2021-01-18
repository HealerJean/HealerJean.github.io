package com.healerjean.proj.server;

import com.healerjean.proj.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;


@Slf4j
public class UserServiceImpl implements UserService.Iface {


    private final static String NAME = "HealerJean";

    @Override
    public String getName(int id) throws TException {
        log.info("用户Id：{}", id);
        return NAME + id;
    }

    @Override
    public boolean isExist(String name) throws TException {
        log.info("请求用户名：{}", name);
        return NAME.equals(name);
    }
}
