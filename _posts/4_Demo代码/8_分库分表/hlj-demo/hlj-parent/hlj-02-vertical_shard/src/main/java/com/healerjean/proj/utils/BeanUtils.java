package com.healerjean.proj.utils;

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
        }
        return dto;
    }
}
