package com.healerjean.proj.data.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.utils.db.CommonResultHandler;

/**
 * UserDemoDao
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55
 */
public interface UserDemoDao extends IService<UserDemo> {

    /**
     * 流查询
     *
     * @param queryWrapper  queryWrapper
     * @param resultHandler resultHandler
     */
    void queryUserDemoByStream(QueryWrapper<UserDemo> queryWrapper, CommonResultHandler<UserDemo> resultHandler);
}
