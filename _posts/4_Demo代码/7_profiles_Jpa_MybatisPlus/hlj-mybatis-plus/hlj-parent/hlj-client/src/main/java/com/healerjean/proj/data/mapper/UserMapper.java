package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.config.keycenter.one.CustomTypeHandler;
import com.healerjean.proj.data.entity.User;
import com.healerjean.proj.dto.UserDTO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName UserMapper
 * @date 2020/3/5  20:08.
 * @Description
 */
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from user where name = #{name}")
    //写不写下面的这行都行
    // @ResultType(UserDTO.class)
    @Results(
            @Result(property = "userId", column = "id")
    )
    List<UserDTO> selectUserDtoList(UserDTO userDTO);


    @Select({
            "<script>",
            "select * from user where",
            "<if test='ids != null and ids.size > 0'> ",
            " id in",
            "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'>",
            " #{item} ",
            "</foreach>",
            "</if>",
            "</script>"
    })
    List<UserDTO> selectListByScript(UserDTO userDTO);


    List<UserDTO> selectByMappeXml(UserDTO userDTO);



    @Select("select * from user where id = 1")
    UserDTO queryLocalDate();


    List<UserDTO> selectLocalDateTimeByMappeXml(UserDTO userDTO);





    @Results({
            @Result(column = "email", property = "email", typeHandler = CustomTypeHandler.class),
            @Result(column = "tel_phone", property = "telPhone", typeHandler = CustomTypeHandler.class)})
    @Select("select * from user where id = #{id}")
    List<User> selectDncryptList(Long id);
}
