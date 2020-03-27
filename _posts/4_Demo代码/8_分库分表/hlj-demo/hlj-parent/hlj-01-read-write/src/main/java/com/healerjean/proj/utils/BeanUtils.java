package com.healerjean.proj.utils;

import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.User;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {


    public static UserDTO demoToDTO(User user) {
        UserDTO dto = new UserDTO();
        if (user != null) {
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setCity(user.getCity());
            dto.setStatus(user.getStatus());
            dto.setCreateTime(user.getCreateTime());
            dto.setUpdateTime(user.getUpdateTime());
        }
        return dto;
    }

    public static User dtoToUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        user.setCreateTime(userDTO.getCreateTime());
        user.setUpdateTime(userDTO.getUpdateTime());
        return user;
    }
}
