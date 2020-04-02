package com.healerjean.proj.service.impl;

import com.healerjean.proj.dao.mapper.CompanyMapper;
import com.healerjean.proj.dao.mapper.UserMapper;
import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.User;
import com.healerjean.proj.service.CompanyService;
import com.healerjean.proj.service.TransactionalService;
import com.healerjean.proj.service.UserService;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName TransactionalServiceImpl
 * @date 2020/4/2  14:52.
 * @Description
 */
@Service
public class TransactionalServiceImpl implements TransactionalService {


    @Resource
    private CompanyService companyService;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CompanyMapper companyMapper;

    /**
     * 分库分表也是有事务的，如果跑出了异常，则都不能成功
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dbTransactional(UserDTO userDTO, CompanyDTO companyDTO) {
        System.out.println("----------------开始进入事务");
        userService.insert(userDTO);
        companyService.insert(companyDTO);
        System.out.println("---------------TransactionTypeHolder" + TransactionTypeHolder.get());
        int i = 1 / 0;
    }


    /**
     * 事务隔离性测试
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void isolationTransactional(UserDTO userDTO, CompanyDTO companyDTO) {
        System.out.println("----------------开始进入事务");
        User  user =  userMapper.selectById(userDTO.getId());
        userService.updateSQL(userDTO.getId(), UUID.randomUUID().toString().replace("-", "").substring(0,5));

        /** 独立事务 失败，因为上面有乐观锁 */
        userService.updateSQLRequiresNewTransactional(userDTO.getId(), UUID.randomUUID().toString().replace("-", "").substring(0,5));

        companyService.insert(companyDTO);
    }
}
