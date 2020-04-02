package com.healerjean.proj.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.pojo.User;
import com.healerjean.proj.pojo.UserRefCompany;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/11/14  下午12:54.
 * 类描述：
 */
public interface UserMapper extends BaseMapper<User> {


    List<UserRefCompany> leftJoin();


    List<UserRefCompany> groupByCity();
}
