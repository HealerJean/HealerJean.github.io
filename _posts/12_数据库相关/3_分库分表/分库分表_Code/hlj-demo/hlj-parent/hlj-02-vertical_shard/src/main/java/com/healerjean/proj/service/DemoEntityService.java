package com.healerjean.proj.service;


import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.dto.UserDTO;

import java.util.List;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface DemoEntityService {


    DemoDTO insert(DemoDTO demoEntity);

    DemoDTO findById(Long id);

    List<DemoDTO> list();


    /**
     * 测试多个数据库事务
     */
    void dbTransactional(UserDTO userDTO, CompanyDTO companyDTO);

}
