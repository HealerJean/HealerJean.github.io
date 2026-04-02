package com.healerjean.proj.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healerjean.proj.config.keycenter.SecretTypeHandler;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author HealerJean
 * @ClassName User
 * @date 2020/3/5  16:11.
 * @Description
 */
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;


    /**
     * 有了这个数据库 BaseMapper 插入的时候才能加密
     */
    @TableField(
            value = "`tel_phone`",
            typeHandler = SecretTypeHandler.class
    )
    private String telPhone;

    private String email;

    /**
     * 添加日期进行测试
     */
    private LocalDate createDate;

    private LocalDateTime createTime;



}
