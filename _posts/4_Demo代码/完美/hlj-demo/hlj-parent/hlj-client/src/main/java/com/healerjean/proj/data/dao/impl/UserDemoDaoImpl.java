package com.healerjean.proj.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.data.dao.UserDemoDao;
import com.healerjean.proj.data.mapper.UserDemoMapper;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.utils.db.CommonResultHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * UserDemoDaoImpl
 *
 * @author zhangyujin
 * @date 2023/6/14  15:33.
 */
@Service
public class UserDemoDaoImpl extends ServiceImpl<UserDemoMapper, UserDemo> implements UserDemoDao {

    /**
     * userDemoMapper
     */
    @Resource
    private UserDemoMapper userDemoMapper;



    /**
     * 流查询
     *
     * @param queryWrapper  queryWrapper
     * @param resultHandler resultHandler
     */
    @Override
    public void queryUserDemoByStream(QueryWrapper<UserDemo> queryWrapper, CommonResultHandler<UserDemo> resultHandler) {
        userDemoMapper.queryUserDemoByStream(queryWrapper, resultHandler);
    }
}
