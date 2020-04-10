package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumeService;
import com.healerjean.proj.service.FeignProviderService;
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
@Api(description = "服务消费者控制器")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerController extends BaseController {

    @Value("${hlj.server.providerName}")
    private String serverProviderName;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FeignProviderService feignProviderService;
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

    /**
     * 占位符{1}
     */
    @GetMapping(value = "/c_get_url")
    public UserDTO userUrl() {
        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity("http://" + serverProviderName + "/api/provider/urlGet?name={1}", UserDTO.class, "HealerJean");
        UserDTO body = responseEntity.getBody();
        return body;
    }

    /**
     * map传参占位符{name}
     */
    @RequestMapping(value = "c_get_map", method = RequestMethod.GET)
    public UserDTO userUrlMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "HealerJean");
        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity("http://" + serverProviderName + "/api/provider/urlGet?name={name}", UserDTO.class, map);
        UserDTO body = responseEntity.getBody();
        return body;
    }

    @GetMapping(value = "/c_post")
    public UserDTO postFirst() {
        UserDTO user = new UserDTO();
        user.setName("HealerJean");
        ResponseEntity<UserDTO> responseEntity =
                restTemplate.postForEntity("http://" + serverProviderName + "/api/provider/urlPost", user, UserDTO.class);
        UserDTO body = responseEntity.getBody();
        return body;
    }

    @GetMapping(value = "/c_put")
    public UserDTO put() {
        UserDTO user = new UserDTO();
        user.setName("HealerJean");
        String id = "2";
        restTemplate.put("http://" + serverProviderName + "/api/provider/put/{1}", user, id);
        return user;
    }

    @GetMapping(value = "c_delete")
    public UserDTO delete() {
        String id = "2";
        restTemplate.delete("http://" + serverProviderName + "/api/provider/delete/{1}", id);
        return null;
    }

    /**
     * restTemplate.getForObject 传参形式和getForEntity是一样的,只不过不到需要.getBody了
     */
    @GetMapping(value = "c_getForObjectTest")
    public UserDTO getForObject() {
        UserDTO userDTO = restTemplate.getForObject("http://" + serverProviderName + "/api/provider/urlGet?name={1}", UserDTO.class, "HealerJean");
        return userDTO;
    }



    /**
     * 测试服务降级
     */
    @GetMapping(value = "hystrix/fallBack")
    public String hystrixFallBack() {
        return consumeService.hystrixFallBack();
    }



    /**
     * 测试申明式服务调用
     */
    @GetMapping(value = "feign/connectProvider")
    public String feignConnectProvider() {
        return feignProviderService.connectProvider();
    }


}


