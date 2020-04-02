package com.healerjean.proj.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dao.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.User;
import com.healerjean.proj.pojo.UserRefCompany;
import com.healerjean.proj.service.UserService;
import com.healerjean.proj.utils.BeanUtils;
import com.healerjean.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


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

    @Override
    public UserDTO insert(UserDTO userDTO) {
        User user = BeanUtils.dtoToUser(userDTO);
        user.setStatus(StatusEnum.生效.code);
        userMapper.insert(user);
        userDTO.setId(user.getId());
        return userDTO;
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userMapper.selectById(id);
        return user == null ? null : BeanUtils.userToDTO(user);
    }

    /**
     * 成功
     */
    @Override
    public List<UserDTO> list() {
        List<User> users = userMapper.selectList(null);
        List<UserDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::userToDTO).collect(Collectors.toList());
        }
        return list;
    }


    /**
     * limit 成功
     */
    @Override
    public List<UserDTO> limit(UserDTO userDTO) {
        Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
                .orderByDesc(User::getCreateTime)
                .last("limit " + userDTO.getPageNow() + ", " + userDTO.getPageSize());
        List<User> users = userMapper.selectList(userWrapper);
        List<UserDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::userToDTO).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * groupBy 成功
     */
    @Override
    public List<UserRefCompany>  group() {
        List<UserRefCompany> list = userMapper.groupByCity();
        return list;
    }


    /**
     * between 成功
     */
    @Override
    public List<UserDTO> between() {
        Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
                .between(User::getAge, 1, 3)
                .orderByDesc(User::getCreateTime);
        List<User> users = userMapper.selectList(userWrapper);
        List<UserDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::userToDTO).collect(Collectors.toList());
        }
        return list;
    }


    /**
     * left join 设置了绑定关系后成功
     */
    @Override
    public List<UserRefCompany> leftJoin() {
        List<UserRefCompany> userRefCompanies = userMapper.leftJoin();
        return userRefCompanies;
    }




}
