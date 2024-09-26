package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * BatchFunctionInvokeBO
 *
 * @author zhangyujin
 * @date 2024/9/24
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BatchFunctionInvokeBO<REQ, RES> extends BatchInvokeBO {

    /**
     * batchFunctions
     */
    private List<BatchFunctionBO<REQ, RES>> batchFunctions;


    /**
     * functionOf
     *
     * @param batchFunctions batchFunctions
     * @return {@link BatchFunctionInvokeBO}
     */
    public static <REQ, RES> BatchFunctionInvokeBO<REQ, RES> of(List<BatchFunctionBO<REQ, RES>> batchFunctions) {
        BatchFunctionInvokeBO<REQ, RES> invoker = new BatchFunctionInvokeBO<>();
        invoker.setBatchFunctions(batchFunctions);
        invoker.setCountDownLatch(new CountDownLatch(batchFunctions.size()));
        return invoker;
    }


}
