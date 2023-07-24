package com.healerjean.proj.liteflow.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DemoContext
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Accessors(chain = true)
@Data
public class DemoContext {

    /**
     * 业务类型
     */
    private String businessType;
}
