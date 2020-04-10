package com.healerjean.proj.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healerjean.proj.config.keycenter.one.CustomTypeHandler;
import lombok.Data;

import java.util.Date;

/**
 * @author HealerJean
 * @ClassName User
 * @date 2020/3/5  16:11.
 * @Description
 */
@Data
@TableName(autoResultMap = true)
public class User {
    private Long id;
    private String name;
    private Integer age;

    @TableField(
            value = "`tel_phone`",
            typeHandler = CustomTypeHandler.class
    )
    private String telPhone;

    @TableField(typeHandler = CustomTypeHandler.class)
    private String email;

    /**
     * 添加日期进行测试
     */
    private Date createDate;
    private Date createTime;
}
