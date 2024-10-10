package com.healerjean.proj.data.bo.batch.poll;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * AsyncThreadBO
 *
 * @author zhangyujin
 * @date 2024/9/26
 */
@Accessors(chain = true)
@Data
public class PoolTaskBO<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2145646221702517887L;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务结果
     */
    private T taskResult;

}
