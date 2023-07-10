package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.healerjean.proj.data.po.UserDemo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

/**
 * UserDemoMapper
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55
 */
public interface UserDemoMapper extends BaseMapper<UserDemo> {


    /**
     * 流式查询
     * @param queryWrapper queryWrapper
     */
    Cursor<UserDemo> queryUserDemoByStream(@Param(Constants.WRAPPER) QueryWrapper<UserDemo> queryWrapper);
}
