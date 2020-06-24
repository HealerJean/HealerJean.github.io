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

    // moke （非业务异常） 服务接口调用失败Mock实现类名，该Mock类必须有一个无参构造函数，与Local的区别在于，Local总是被执行，而Mock只在出现非业务异常(比如超时，网络异常等)时执行，Local在远程调用之前执行，Mock在远程调用后执行。
    @Reference(version = "0.1", group = "inter_one", mock = "com.healerjean.proj.service.MockDubboService")
    private ProviderDubboService providerDubboService;

    @ApiOperation(value = "connect",
            notes = "connect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "connect")
    public String connectProvider(String name) {
        log.info("服务消费者控制器--------connect--------");
        String connect = providerDubboService.connect(name);
        log.info("服务消费者控制器--------返回信息--------{}", connect);
        return connect;
    }

}


