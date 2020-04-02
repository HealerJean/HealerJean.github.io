package com.healerjean.proj.service;

import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.UserDTO;

/**
 * @author HealerJean
 * @ClassName TransactionalService
 * @date 2020/4/2  14:52.
 * @Description
 */
public interface TransactionalService {


    /**
     * 测试多个数据库事务
     */
    void dbTransactional(UserDTO userDTO, CompanyDTO companyDTO);

    /**
     * 事务隔离性测试
     */
    void isolationTransactional(UserDTO userDTO, CompanyDTO companyDTO);
    
}
