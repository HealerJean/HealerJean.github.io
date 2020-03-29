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

    @Override
    public List<UserDTO> list() {
        List<User> users = userMapper.selectList(null);
        List<UserDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::userToDTO).collect(Collectors.toList());
        }
        return list;
    }


    @Override
    public List<UserDTO> limit() {
        Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
                .orderByAsc(User::getId)
                .last("limit 1, 5");
        List<User> users = userMapper.selectList(userWrapper);
        List<UserDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::userToDTO).collect(Collectors.toList());
        }
        return list;    }

    @Override
    public List<UserRefCompany>  leftJoin() {
        List<UserRefCompany>  userRefCompanies = userMapper.leftJoin();
        return userRefCompanies;
    }


    /**
     * 成功
     */
    @Override
    public void group() {
        Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
                .select(User::getCity )
                .groupBy(User::getCity);
        List<Object> citys = userMapper.selectObjs(userWrapper);

        log.info("citys：{}", citys);
    }




}
