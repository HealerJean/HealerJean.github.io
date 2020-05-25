package com.healerjean.proj.service.impl;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO login(Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("HealerJean");
        userDTO.setPassword("123456");
        log.info("UserServiceImpl--------{}", userDTO);
        return userDTO;
    }

}
