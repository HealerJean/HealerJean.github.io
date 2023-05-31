package com.healerjean.proj.service.bizlog.data.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User实体对象
 *
 * @author zhangyujin
 * @date 2023/5/31  16:05.
 */
@Accessors(chain = true)
@Data
public class User {
    /**
     * 用户名
     */
    private String name;

    /**
     * userId
     */
    private String userId;
}
