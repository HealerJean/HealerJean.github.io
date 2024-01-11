package com.healerjean.proj.data.dao;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.data.po.UserDemo;

/**
 * UserDemoDao
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55
 */
public interface UserDemoDao extends IService<UserDemo> {

    int resCountUpdate(UpdateWrapper<UserDemo>updateWrapper);

}
