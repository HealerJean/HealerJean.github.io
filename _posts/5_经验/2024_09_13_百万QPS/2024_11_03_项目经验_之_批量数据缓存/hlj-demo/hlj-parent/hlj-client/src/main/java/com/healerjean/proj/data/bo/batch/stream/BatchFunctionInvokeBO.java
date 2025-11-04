package com.healerjean.proj.data.bo.batch.stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * BatchFunctionInvokeBO
 *
 * @author zhangyujin
 * @date 2024/9/24
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BatchFunctionInvokeBO extends BatchInvokeBO {

    /**
     * batchFunctions
     */
    private List<BatchFunctionBO> batchFunctions;


    /**
     * functionOf
     *
     * @param batchFunctions batchFunctions
     * @return {@link BatchFunctionInvokeBO}
     */
    public static  BatchFunctionInvokeBO of(Executor executor, List<BatchFunctionBO> batchFunctions) {
        BatchFunctionInvokeBO invoker = new BatchFunctionInvokeBO();
        invoker.setBatchFunctions(batchFunctions);
        invoker.setExecutor(executor);
        return invoker;
    }


}
