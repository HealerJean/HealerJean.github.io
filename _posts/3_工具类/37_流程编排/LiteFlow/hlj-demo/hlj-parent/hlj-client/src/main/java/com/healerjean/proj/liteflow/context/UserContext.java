package com.healerjean.proj.liteflow.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * UserContext
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Accessors(chain = true)
@Data
public class UserContext {

    /**
     * userType
     */
    private String userType;
}
