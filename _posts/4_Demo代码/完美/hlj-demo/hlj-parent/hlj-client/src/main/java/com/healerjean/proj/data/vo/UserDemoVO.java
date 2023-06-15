package com.healerjean.proj.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * UserDemoVO
 *
 * @author zhangyujin
 * @date 2023/6/14  11:31.
 */
@ApiModel(value = "UserDemoVO视图" ,description = "UserDemoVO视图描述")
@Accessors(chain = true)
@Data
public class UserDemoVO implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7590059129464368394L;

    public UserDemoVO() {
    }

    /**
     * Id
     */
    private Long id;

    /**
     * 名字
     */
    @ApiModelProperty(value = "name名字", notes = "name的字段描述")
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
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
}

