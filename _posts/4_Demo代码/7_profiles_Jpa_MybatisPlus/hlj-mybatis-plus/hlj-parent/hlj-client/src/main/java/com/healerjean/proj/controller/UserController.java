package com.healerjean.proj.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.data.entity.User;
import com.healerjean.proj.data.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName UserController
 * @date 2020/3/5  20:35.
 * @Description
 */
@RestController
@RequestMapping("hlj/user")
@Api(description = "用户管理")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping(value = "selectById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean selectById(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getId());
        log.info("用户管理--------selectById：【{}】", JsonUtils.toJsonString(user));
        return ResponseBean.buildSuccess(user);
    }

    @GetMapping(value = "selectList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean selectList(UserDTO userDTO) {
        List<User> users = userMapper.selectList(null);
        log.info("用户管理--------selectLis：【{}】", JsonUtils.toJsonString(users));
        return ResponseBean.buildSuccess(users);
    }


    @GetMapping(value = "insert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean insert(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        userMapper.insert(user);
        log.info("用户管理--------insert：【{}】", JsonUtils.toJsonString(user));
        return ResponseBean.buildSuccess(user);
    }



}
