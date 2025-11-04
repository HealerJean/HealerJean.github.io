package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserDemoBO
 *
 * @author zhangyujin
 * @date 2023/6/14  10:57.
 */
@Accessors(chain = true)
@Data
public class UserDemoBO implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4105795350093398821L;


    /**
     * Id
     */
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
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 1 有效 0 失效
     */
    private Integer validFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
