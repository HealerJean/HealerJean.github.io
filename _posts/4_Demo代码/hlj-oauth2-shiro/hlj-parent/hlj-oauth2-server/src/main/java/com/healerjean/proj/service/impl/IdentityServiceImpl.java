package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.dto.LoginUserDTO;
import com.healerjean.proj.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName 用户认证信息接口实现
 * @Date 2019/10/18  17:06.
 * @Description
 */
@Service
public class IdentityServiceImpl implements IdentityService {


    /**
     * 根据用户ID获取当前登陆用户信息
     * 1、用户基本信息
     * 2、用户所处部门信息
     * 3、角色集合
     * 4、获取角色所对应的所有菜单（这里的菜单指的是所有的url，包括前台菜单）
     */
    @Override
    public LoginUserDTO getUserInfo(Long userId) {
        if (userId == null || userId < 0) {
            throw new BusinessException("用户ID不能为空");
        }
        //1、用户基本信息
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        return loginUserDTO;
    }
}
