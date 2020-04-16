package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumerFeignServerService;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName FeignConsumerController
 * @date 2020/4/14  16:09.
 * @Description
 */
@Api(description = "服务消费者_3001_控制器-声明式服务调用Controller")
@RestController
@RequestMapping("api/consumer/feign/zuul")
@Slf4j
public class ConsumerFeignZuulController {

    @Autowired
    private ConsumerFeignServerService consumerFeignServerService;

    /**
     * 测试申明式服务调用
     */
    @GetMapping(value = "invokeInter")
    public String invokeInter() {
        String str1 = consumerFeignServerService.reequestParam("healerjean");
        UserDTO userDTO1 = consumerFeignServerService.requestHeader(1L, "healerjean");
        UserDTO userDTO2 = consumerFeignServerService.requestBody(new UserDTO(1L, "healerjean"));
        UserDTO userDTO3 = consumerFeignServerService.post(new UserDTO(1L, "healerjean"));
        return "str1 = " + str1 + "\n "
                + "str2 = " + JsonUtils.toJsonString(userDTO1) + "\n "
                + "str3 = " + JsonUtils.toJsonString(userDTO2) + "\n "
                + "str4 = " + JsonUtils.toJsonString(userDTO3) + "\n ";
    }
}
