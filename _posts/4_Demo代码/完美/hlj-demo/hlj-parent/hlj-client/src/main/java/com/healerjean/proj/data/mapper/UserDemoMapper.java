package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.healerjean.proj.data.po.UserDemo;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * UserDemoMapper
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55
 */
public interface UserDemoMapper extends BaseMapper<UserDemo> {


    /**
     * 流式擦HX
     * @param queryWrapper queryWrapper
     * @param handler handler
     */
    @Select("select * from user_demo ${ew.customSqlSegment}")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(UserDemo.class)
    void queryUserDemoByStream(@Param(Constants.WRAPPER) QueryWrapper<UserDemo> queryWrapper, ResultHandler<UserDemo> handler);

}
