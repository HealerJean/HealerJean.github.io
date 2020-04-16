package com.healerjean.proj.service;

import com.healerjean.proj.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName FeIgnProviderService
 * @date 2020/4/9  12:40.
 * @Description
 */
@RequestMapping("api/provider/feign/zuul")
public interface FeignServerService {

    @GetMapping("reequestParam")
    String reequestParam(@RequestParam("name") String name);

    @GetMapping("requestHeader")
    UserDTO requestHeader(@RequestHeader("id") Long id, @RequestHeader("name") String name);

    @PostMapping("requestBody")
    UserDTO requestBody(@RequestBody UserDTO userDTO);

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    UserDTO post(UserDTO userDTO);

}
