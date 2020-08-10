package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.FeignServerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务提供者_2001_声明式服务调用Controller")
@RestController
@RequestMapping("api/provider/feign/zuul")
@Slf4j
public class ProviderFeignZuulController extends BaseController implements FeignServerService {

    /**
     * reequestParam 参数接收
     */
    @GetMapping("reequestParam")
    @Override
    public String reequestParam(@RequestParam("name") String name) {
        log.info("声明式服务调用Controller--------reequestParam 参数接收--------请求参数：{}", name);
        return "声明式服务调用Controller--------reequestParam 参数接收--------成功 ：" + name;
    }

    /**
     * requestHeader 参数接收
     */
    @GetMapping("requestHeader")
    @Override
    public UserDTO requestHeader(@RequestHeader("id") Long id, @RequestHeader("name") String name) {
        log.info("声明式服务调用Controller--------requestHeader 参数接收--------请求参数：{}", name);
        return new UserDTO(id, name);
    }


    /**
     *  requestBody 参数接收
     */
    @PostMapping("requestBody")
    @Override
    public UserDTO requestBody(@RequestBody UserDTO userDTO) {
        log.info("声明式服务调用Controller--------requestBody 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }

    /**
     *  post 参数接收(接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    @Override
    public UserDTO post(UserDTO userDTO) {
        log.info("声明式服务调用Controller--------post 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }
}


