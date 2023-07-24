package com.healerjean.proj.liteflow.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * NodePramsBO
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Accessors(chain = true)
@Data
public class NodePramsBO {

    /**
     * name
     */
    private String name;

    /**
     * age
     */
    private Integer age;

}
