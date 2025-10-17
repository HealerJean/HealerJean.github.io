package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * BigKeyDTO
 *
 * @author zhangyujin
 * @date 2024/2/7
 */
@Accessors(chain = true)
@Data
public class BigKeyBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4232260671431928412L;

    /**
     * 缓存汇总key
     */
    private String uuidKey;

    /**
     * 单独Key
     */
    private String singleKey;


}
