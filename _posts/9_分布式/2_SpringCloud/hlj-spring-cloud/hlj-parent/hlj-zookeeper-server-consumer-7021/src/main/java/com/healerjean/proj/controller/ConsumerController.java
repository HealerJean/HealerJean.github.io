package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务消费者_7021_控制器")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerController extends BaseController {

    @Value("${hlj.server.providerName}")
    private String serverProviderName;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConsumeService consumeService;

    @ApiOperation(value = "connect",
            notes = "connect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ServiceInstance.class)
    @GetMapping(value = "connect")
    public String connectProvider() {
        log.info("服务消费者控制器--------connect--------");
        return restTemplate.getForEntity("http://" + serverProviderName + "/api/provider/connect/", String.class).getBody();
    }

}


