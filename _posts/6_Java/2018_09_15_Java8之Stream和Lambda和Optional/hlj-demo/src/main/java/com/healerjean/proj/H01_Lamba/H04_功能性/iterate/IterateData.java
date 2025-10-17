package com.healerjean.proj.H01_Lamba.H04_功能性.iterate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

/**
 * IterateData
 *
 * @author zhangyujin
 * @date 2025/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterateData<I, R> {
    /**
     * 可选：标识不同的处理逻辑
     */
    private String type;

    /**
     * 迭代上下文
     */
    private IterateContext<I, R> iterateContext;

    /**
     * 消费上下文的逻辑
     */
    private Consumer<IterateContext<I, R>> consumer;
}