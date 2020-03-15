package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.data.entity.User;
import com.healerjean.proj.dto.UserDTO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName UserMapper
 * @date 2020/3/5  20:08.
 * @Description
 */
public interface UserMapper  extends BaseMapper<User> {


    @Select("select * from user where name = #{name}")
    //写不写下面的都行
    // @ResultType(UserDTO.class)
    @Results(
            @Result(property = "userId", column = "id")
    )
    List<UserDTO> selectUserDtoList(UserDTO userDTO);



    @Select({
            "<script>",
            "select * from user where",
            "id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<UserDTO> selectListByScript(UserDTO userDTO);
}
