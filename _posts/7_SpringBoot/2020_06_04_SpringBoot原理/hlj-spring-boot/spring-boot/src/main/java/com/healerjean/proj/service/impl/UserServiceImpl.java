package com.healerjean.proj.service.impl;

import com.healerjean.proj.config.AppConfig;
import com.healerjean.proj.config.DataConfig;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
        log.info("UserServiceImpl--------");
    }

    //有多个有参构造器补鞥呢实例化，此时必须有一个午餐构造器
    public UserServiceImpl(AppConfig appConfig) {
        log.info("UserServiceImpl--------appConfig：{}", appConfig);
    }
    public UserServiceImpl(AppConfig appConfig, DataConfig dataConfig) {
        log.info("UserServiceImpl--------appConfig：{}, dataConfig：{}", appConfig,dataConfig);
    }

    @Override
    public UserDTO login(Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("HealerJean");
        userDTO.setPassword("123456");
        log.info("UserServiceImpl--------{}", userDTO);
        return userDTO;
    }


}
