package com.healerjean.proj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.healerjean.proj.service.ProviderDubboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务消费者_3001_控制器")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerController extends BaseController {

    @Reference
    private ProviderDubboService providerDubboService;

    @ApiOperation(value = "connect",
            notes = "connect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "connect")
    public String connectProvider(String name) {
        log.info("服务消费者控制器--------connect--------");
        return providerDubboService.connect(name);
    }

}


