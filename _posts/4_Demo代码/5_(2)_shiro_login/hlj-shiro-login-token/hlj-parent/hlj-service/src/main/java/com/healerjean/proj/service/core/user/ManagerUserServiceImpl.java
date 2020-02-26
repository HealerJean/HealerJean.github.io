package com.healerjean.proj.service.core.user;

import com.healerjean.proj.data.manager.system.SysUserInfoManager;
import com.healerjean.proj.data.pojo.system.SysUserInfo;
import com.healerjean.proj.data.repository.system.SysUserInfoRepository;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.service.core.AbstractUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName ManagerUserServiceImpl
 * @date 2019/10/23  19:25.
 * @Description
 */
@Service
@Slf4j
public class ManagerUserServiceImpl extends AbstractUserServiceImpl {


    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;
    @Autowired
    private SysUserInfoManager sysUserInfoManager;


    @Override
    public void addUser(UserDTO userDTO, LoginUserDTO loginUserDTO) {

        SysUserInfo userInfo = sysUserInfoRepository.findByUserName(userDTO.getUserName());
        if (userInfo != null) {
            throw new BusinessException("用户名已经被注册了");
        }
        if (sysUserInfoRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("邮箱已经被注册了");
        }

        userInfo = new SysUserInfo();
        // 1、保存用户信息
        userInfo.setUserName(userDTO.getUserName());
        userInfo.setRealName(userDTO.getRealName());
        userInfo.setEmail(userDTO.getEmail());
        userInfo.setTelephone(userDTO.getTelephone());
        userInfo.setPassword(userDTO.getPassword());
        userInfo.setUserType(userDTO.getUserType());
        userInfo.setStatus(StatusEnum.生效.code);
        userInfo.setSalt(userDTO.getSalt());
        userInfo.setCreateUser(loginUserDTO.getUserId());
        userInfo.setCreateName(loginUserDTO.getRealName());
        userInfo.setUpdateUser(loginUserDTO.getUserId());
        userInfo.setUpdateName(loginUserDTO.getRealName());
        sysUserInfoManager.insertSelective(userInfo);
    }
}
