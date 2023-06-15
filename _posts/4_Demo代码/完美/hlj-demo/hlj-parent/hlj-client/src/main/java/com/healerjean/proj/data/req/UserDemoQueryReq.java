package com.healerjean.proj.data.req;

import com.healerjean.proj.common.data.dto.OrderByDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * UserDemoQueryReq
 *
 * @author zhangyujin
 * @date 2023/6/14  11:08.
 */
@Accessors(chain = true)
@Data
public class UserDemoQueryReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6727021241708789928L;

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
    private String queryTime;

    /**
     * likeName
     */
    private String likeName;

    /**
     * likePhone
     */
    private String likePhone;


    /**
     * selectFields
     */
    private List<String> selectFields;

    /**
     * 排序字段
     */
    private List<OrderByDTO> orderByList;
}
