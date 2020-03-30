package com.healerjean.proj.service;


import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.UserRefCompany;

import java.util.List;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface UserService {


    UserDTO insert(UserDTO userDTO);

    UserDTO findById(Long id);

    List<UserDTO> list();


    List<UserDTO> limit(UserDTO userDTO);


    List<UserRefCompany>  leftJoin();

    List<UserRefCompany>  group();
}
