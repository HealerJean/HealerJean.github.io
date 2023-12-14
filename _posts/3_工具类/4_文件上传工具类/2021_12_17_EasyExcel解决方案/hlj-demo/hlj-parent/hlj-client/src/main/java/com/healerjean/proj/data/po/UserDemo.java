package com.healerjean.proj.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserDemo
 *
 * @author HealerJean
 * @date 2023-06-21 01:06:40
 */
@Accessors(chain = true)
@Data
public class UserDemo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6025408340884821367L;

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名字
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 1 有效 0 失效
     */
    private Integer validFlag;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
