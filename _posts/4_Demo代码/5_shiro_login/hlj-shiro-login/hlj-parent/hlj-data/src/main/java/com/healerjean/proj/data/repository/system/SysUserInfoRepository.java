package com.healerjean.proj.data.repository.system;

import com.healerjean.proj.data.pojo.system.SysUserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * @author HealerJean
 * @ClassName SysUserInfoRepository
 * @date 2019/10/18  14:01.
 * @Description
 */
public interface SysUserInfoRepository extends CrudRepository<SysUserInfo, Long> {

    SysUserInfo findByUserName(String userName);

    boolean existsByEmail(String email);


    boolean existsByUpdateName(String userName);
}
