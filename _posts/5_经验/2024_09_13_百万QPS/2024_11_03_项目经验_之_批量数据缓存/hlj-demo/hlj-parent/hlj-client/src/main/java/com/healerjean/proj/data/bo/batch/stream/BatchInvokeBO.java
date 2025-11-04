package com.healerjean.proj.data.bo.batch.stream;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.Executor;

/**
 * BatchInvokeBO
 *
 * @author zhangyujin
 * @date 2024/9/24
 */
@Accessors(chain = true)
@Data
public class BatchInvokeBO {


    /**
     * executor
     */
    private Executor executor;


}
