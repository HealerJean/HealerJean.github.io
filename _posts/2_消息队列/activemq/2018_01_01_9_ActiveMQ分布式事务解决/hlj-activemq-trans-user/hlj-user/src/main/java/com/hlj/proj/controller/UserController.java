package com.hlj.proj.controller;

import com.hlj.proj.api.user.UserService;
import com.hlj.proj.dto.ResponseBean;
import com.hlj.proj.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName UserController
 * @date 2019/9/9  16:43.
 * @Description
 */
@RestController
@RequestMapping("hlj")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("user/add")
    public ResponseBean addUser(UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseBean.buildSuccess(userDTO);
    }
}
