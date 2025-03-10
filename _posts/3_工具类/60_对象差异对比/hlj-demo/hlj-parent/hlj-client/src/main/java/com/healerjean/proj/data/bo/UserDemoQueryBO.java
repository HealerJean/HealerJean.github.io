package com.healerjean.proj.data.bo;

import com.healerjean.proj.common.data.bo.OrderByBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UserDemoQueryBO
 *
 * @author zhangyujin
 * @date 2023/6/14  11:03.
 */
@Accessors(chain = true)
@Data
public class UserDemoQueryBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8922680389790825534L;

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
     * 1 有效 0 失效
     */
    private Integer validFlag;

    /**
     * 创建时间
     */
    private LocalDateTime queryTime;

    /**
     * likeName
     */
    private String likeName;

    /**
     * likePhone
     */
    private String likePhone;

    /**
     * 最小Id
     */
    private Long minId;

    /**
     * 最大Id
     */
    private Long maxId;

    /**
     * selectFields
     */
    private List<String> selectFields;

    /**
     * 排序字段
     */
    private List<OrderByBO> orderByList;


}


