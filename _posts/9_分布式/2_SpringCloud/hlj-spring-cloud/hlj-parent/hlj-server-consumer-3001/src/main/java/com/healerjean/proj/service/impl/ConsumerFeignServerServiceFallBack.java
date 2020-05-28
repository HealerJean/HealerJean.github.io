package com.healerjean.proj.service.impl;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumerFeignServerService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName ConsumerFeignServerServiceFallBack
 * @date 2020/4/14  17:51.
 * @Description
 */
@Component
public class ConsumerFeignServerServiceFallBack implements ConsumerFeignServerService {


    @Override
    public String reequestParam(String name) {
        return "error";
    }
    @Override
    public UserDTO requestHeader(Long id, String name) {
        return new UserDTO();
    }

    @Override
    public UserDTO requestBody(UserDTO userDTO) {
        return new UserDTO();
    }

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @Override
    public UserDTO post(UserDTO userDTO) {
        return  new UserDTO();
    }
}
