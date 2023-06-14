package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.data.entity.UserDemo;
import com.healerjean.proj.data.mapper.UserMapper;
import com.healerjean.proj.dto.UserDemoDTO;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("hlj/userDemo")
@Api(description = "用户管理")
@Slf4j
public class UserDemoController {

    @Resource
    private UserMapper userMapper;

    @GetMapping(value = "selectById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseBean selectById(UserDemoDTO userDemoDTO) {
        UserDemo userDemo = userMapper.selectById(userDemoDTO.getId());
        log.info("用户管理--------selectById：【{}】", JsonUtils.toJsonString(userDemo));
        return ResponseBean.buildSuccess(userDemo);
    }

    @GetMapping(value = "selectList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseBean selectList(UserDemoDTO userDemoDTO) {
        List<UserDemo> userDemos = userMapper.selectList(null);
        log.info("用户管理--------selectLis：【{}】", JsonUtils.toJsonString(userDemos));
        return ResponseBean.buildSuccess(userDemos);
    }


    @GetMapping(value = "insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseBean insert(UserDemoDTO userDemoDTO) {
        UserDemo userDemo = new UserDemo();
        userDemo.setName(userDemoDTO.getName());
        userDemo.setAge(userDemoDTO.getAge());
        userDemo.setTelPhone(userDemoDTO.getTelPhone());
        userDemo.setEmail(userDemoDTO.getEmail());
        userMapper.insert(userDemo);
        log.info("用户管理--------insert：【{}】", JsonUtils.toJsonString(userDemo));
        return ResponseBean.buildSuccess(userDemo);
    }

}
