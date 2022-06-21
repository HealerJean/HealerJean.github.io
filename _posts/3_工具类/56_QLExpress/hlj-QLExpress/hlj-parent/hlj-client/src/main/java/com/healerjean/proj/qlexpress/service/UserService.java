package com.healerjean.proj.qlexpress.service;

import com.healerjean.proj.qlexpress.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangyujin
 * @date 2022/6/21  21:13.
 */
public class UserService {

    /**
     * 判断用户是否注册
     *
     * @param userDTO 用户dto
     * @return 注册 true，未注册 false
     */
    public UserDTO registerCheck(UserDTO userDTO, boolean login) {
        if (login &&  StringUtils.endsWithIgnoreCase("HealerJean", userDTO.getName())) {
            userDTO.setId(1L);
            return userDTO;
        }
        return null;
    }
}
