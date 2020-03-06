package com.healerjean.proj.service.core;

import com.healerjean.proj.api.core.UserService;
import com.healerjean.proj.data.manager.system.SysUserInfoManager;
import com.healerjean.proj.data.pojo.system.SysUserInfo;
import com.healerjean.proj.data.pojo.system.SysUserInfoQuery;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HealerJean
 * @ClassName AbstractUserServiceImpl
 * @date 2019/10/23  19:26.
 * @Description
 */
public abstract class AbstractUserServiceImpl implements UserService {

    @Autowired
    private SysUserInfoManager sysUserInfoManager;


    /**
     * 注册用户
     */
    @Override
    public void registerUser(UserDTO userDTO) {
    }

    /**
     * 添加用户
     */
    @Override
    public void addUser(UserDTO userDTO, LoginUserDTO loginUserDTO) {
    }

    /**
     * 获取用户
     */
    @Override
    public UserDTO getUserInfo(UserDTO userDTO) {
        if (userDTO == null) {
            throw new ParameterErrorException("参数不能为空");
        }
        SysUserInfo userInfo = null;
        if (userDTO.getUserId() != null) {
            userInfo = sysUserInfoManager.findById(userDTO.getUserId());
            //给用户登录提供 userType判断用户的登录类型（网站人员还是）
        } else if (StringUtils.isNotBlank(userDTO.getUserName())) {
            SysUserInfoQuery query = new SysUserInfoQuery();
            query.setUserName(userDTO.getUserName());
            query.setUserType(userDTO.getUserType());
            userInfo = sysUserInfoManager.findByQueryContion(query);
        } else if (StringUtils.isNotBlank(userDTO.getEmail())) {
            SysUserInfoQuery query = new SysUserInfoQuery();
            query.setEmail(userDTO.getEmail());
            query.setUserType(userDTO.getUserType());
            userInfo = sysUserInfoManager.findByQueryContion(query);
        }
        if (userInfo == null) {
            throw new BusinessException(ResponseEnum.用户不存在);
        }
        return BeanUtils.userInfoToDTO(userInfo);
    }
}

