package com.healerjean.proj.api.core;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.UserDTO;

/**
 * @author HealerJean
 * @ClassName UserService
 * @date 2019/10/18  13:52.
 * @Description
 */
public interface UserService {

    /**
     * 注册用户
     */
    void registerUser(UserDTO userDTO);

    /**
     * 添加用户
     */
    void addUser(UserDTO userDTO, LoginUserDTO loginUserDTO);

    /**
     * 获取用户
     */
    UserDTO getUserInfo(UserDTO userDTO);
}
