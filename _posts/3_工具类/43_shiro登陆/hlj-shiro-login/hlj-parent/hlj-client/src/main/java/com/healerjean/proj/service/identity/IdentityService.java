package com.healerjean.proj.service.identity;


import com.healerjean.proj.dto.user.LoginUserDTO;


/**
 * @author HealerJean
 * @ClassName IdentityService
 * @Date 2019-11-02  00:36.
 * @Description用户认证信息接口
 */

public interface IdentityService {

    /**
     * 根据用户ID获取当前登陆用户信息
     * @param userId
     * @return
     */
    LoginUserDTO getUserInfo(Long userId);
}
