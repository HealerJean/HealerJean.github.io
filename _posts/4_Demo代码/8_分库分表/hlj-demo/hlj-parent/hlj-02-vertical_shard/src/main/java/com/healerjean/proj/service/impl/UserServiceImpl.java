package com.healerjean.proj.service.impl;

import com.healerjean.proj.dao.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.User;
import com.healerjean.proj.service.UserService;
import com.healerjean.proj.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO insert(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        userMapper.insert(user);
        userDTO.setId(user.getId());
        return userDTO;
    }


    @Override
    public UserDTO findById(Long id) {
        User user = userMapper.selectById(id);
        return user == null ? null : BeanUtils.demoToDTO(user);
    }

}
