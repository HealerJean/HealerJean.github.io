package com.healerjean.proj.data.req;

import com.healerjean.proj.common.anno.CollectionIncluded;
import com.healerjean.proj.common.data.ValidateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @date 2023/6/14  11:07.
 */
@Data
public class UserDemoSaveReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6956929596959647085L;

    /**
     * 名字
     */
    @NotBlank(message = "名字 不能为空")
    private String name;
    /**
     * 年龄
     */
    @NotNull(message = "年龄 不能为空")
    private Integer age;
    /**
     * 电话
     */
    @NotBlank(message = "电话 不能为空")
    private String phone;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱 不能为空")
    private String email;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间 不能为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间 不能为空")
    private String endTime;

    /**
     * 1 有效 0 实现
     */
    @CollectionIncluded(message = "必须是1或者0", collections = "0,1", groups = {ValidateGroup.SaveUserDemo.class})
    @NotNull(message = "状态 不能为空")
    private Integer validFlag;
}
