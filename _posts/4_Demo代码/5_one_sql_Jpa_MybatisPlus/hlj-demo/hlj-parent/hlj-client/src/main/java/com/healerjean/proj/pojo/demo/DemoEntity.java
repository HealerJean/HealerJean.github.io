/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.pojo.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 测试实体类;
 */
@Entity
@Table(name = "demo_entity")
@Data
@Accessors(chain = true)
public class DemoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** mybatis-plus如果希望使用数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 姓名 */
    private String name;
    /** 手机号  */
    private String phone;
    /**  邮箱 */
    private String email;
    /** 年龄  */
    private Integer age;
    /**  10可用，99删除  */
    private String status;
    /** 创建人 */
    private Long createUser;
    /** 创建人名称  */
    private String createName;
    /**  创建时间 */
    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private java.util.Date createTime;
    /**  更新人 */
    private Long updateUser;
    /** 更新人名称 */
    private String updateName;
    /**  更新时间 */
    @UpdateTimestamp
    private java.util.Date updateTime;

}





