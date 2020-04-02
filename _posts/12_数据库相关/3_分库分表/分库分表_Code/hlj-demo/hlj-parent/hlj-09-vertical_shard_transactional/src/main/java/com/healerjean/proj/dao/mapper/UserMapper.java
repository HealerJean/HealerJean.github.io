package com.healerjean.proj.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.pojo.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/11/14  下午12:54.
 * 类描述：
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 更新语句
     */
    void updateSQL(@Param("id") Long id,
                   @Param("name") String name);
}
