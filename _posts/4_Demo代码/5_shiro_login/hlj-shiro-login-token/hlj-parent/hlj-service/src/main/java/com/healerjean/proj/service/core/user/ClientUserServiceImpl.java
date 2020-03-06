package com.healerjean.proj.service.core.user;

import com.healerjean.proj.data.manager.system.SysUserInfoManager;
import com.healerjean.proj.data.pojo.system.SysUserInfo;
import com.healerjean.proj.data.repository.system.SysUserInfoRepository;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.service.core.AbstractUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author HealerJean
 * @ClassName UserService
 * @date 2019/10/18  13:52.
 * @Description
 */
@Service
@Slf4j
public class ClientUserServiceImpl extends AbstractUserServiceImpl {

    @Autowired
    private SysUserInfoManager sysUserInfoManager;
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;


    /**
     * 注册用户
     * 1、保存用户信息
     * 2、保存阿里妈妈信息
     * 3、保存用户与阿里妈妈的信息关联表
     * 4、保存推广位信息
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public void registerUser(UserDTO userDTO) {
        SysUserInfo userInfo = sysUserInfoRepository.findByUserName(userDTO.getUserName());
        if (userInfo != null) {
            throw new BusinessException("用户名已经被注册了");
        }
        if (sysUserInfoRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("邮箱已经被注册了");
        }
        // 1、保存用户信息
        userInfo = new SysUserInfo();
        userInfo.setUserName(userDTO.getUserName());
        userInfo.setRealName(userDTO.getRealName());
        userInfo.setEmail(userDTO.getEmail());
        userInfo.setTelephone(userDTO.getTelephone());
        userInfo.setPassword(userDTO.getPassword());
        userInfo.setUserType(userDTO.getUserType());
        userInfo.setStatus(StatusEnum.生效.code);
        userInfo.setSalt(userDTO.getSalt());
        sysUserInfoManager.insertSelective(userInfo);
    }


}
