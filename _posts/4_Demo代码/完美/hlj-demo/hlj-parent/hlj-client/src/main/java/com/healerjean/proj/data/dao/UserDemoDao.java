package com.healerjean.proj.data.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.data.po.UserDemo;
import org.apache.ibatis.cursor.Cursor;

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
     */
    Cursor<UserDemo> queryUserDemoByStream(QueryWrapper<UserDemo> queryWrapper);
}
