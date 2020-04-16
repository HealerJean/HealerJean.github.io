package com.healerjean.proj.service;


import com.healerjean.proj.dto.LoginUserDTO;

public interface IdentityService {

    /**
     * 根据用户ID获取当前登陆用户信息
     */
    LoginUserDTO getUserInfo(Long userId);

}
