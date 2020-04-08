package com.healerjean.proj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务提供者Home控制器")
@RestController
@RequestMapping("api/provider")
@Slf4j
public class ProviderController extends BaseController {


    @Autowired
    private DiscoveryClient discoveryClient;


    @ApiOperation(value = "connect",
            notes = "connect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ServiceInstance.class)
    @GetMapping(value = "connect")
    public ServiceInstance index() {
        log.info("服务提供者Home控制器--------connect--------");
        ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
        log.info("host：【{}】,serviceId：【{}】", serviceInstance.getHost(), serviceInstance.getServiceId());
        return serviceInstance;
    }
}


