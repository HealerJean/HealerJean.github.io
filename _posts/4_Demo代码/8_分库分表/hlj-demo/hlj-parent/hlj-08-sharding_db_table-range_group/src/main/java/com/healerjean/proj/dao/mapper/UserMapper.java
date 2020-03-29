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


    @Select("select u.id," +
            "       u.name ," +
            "       c.id as companyId," +
            "       c.name as companyName," +
            "       c.company_name_english as companyNameEnglish" +
            "       from user u" +
            "    left join  company c  on u.name = c .name")
    List<UserRefCompany> leftJoin();
}
