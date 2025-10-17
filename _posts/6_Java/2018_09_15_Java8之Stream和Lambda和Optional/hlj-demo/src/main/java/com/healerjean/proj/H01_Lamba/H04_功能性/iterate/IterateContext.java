package com.healerjean.proj.H01_Lamba.H04_功能性.iterate;

/**
 * IterationContext
 *
 * @author zhangyujin
 * @date 2025/9/11
 */

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用迭代上下文，用于在迭代过程中传递数据。
 */
@Accessors(chain = true)
@Data
public class IterateContext<I, R> {

    /**
     * 当前迭代项的类型 (例如 DemoBO, Integer 等)
     */
    private I currentItem;

    /**
     * 迭代过程中产生的累积结果类型 (例如 Map, List 等)
     */
    private R result;



    @Override
    public String toString() {
        return "IterationContext{" +
                "currentItem=" + currentItem +
                ", result=" + result +
                '}';
    }
}
