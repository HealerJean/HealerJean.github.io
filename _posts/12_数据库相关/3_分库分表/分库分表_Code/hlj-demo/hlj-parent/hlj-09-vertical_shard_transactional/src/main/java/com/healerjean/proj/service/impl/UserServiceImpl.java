package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dao.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.User;
import com.healerjean.proj.service.UserService;
import com.healerjean.proj.utils.BeanUtils;
import com.healerjean.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public void updateSQL(Long id, String name) {
        userMapper.updateSQL(id, name);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateSQLRequiresNewTransactional(Long id, String name) {
        userMapper.updateSQL(id, name);
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
}
