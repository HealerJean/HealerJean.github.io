package com.healerjean.proj.data.bo.batch.stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * BatchInvokeBO
 *
 * @author zhangyujin
 * @date 2024/9/24
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BatchConsumerInvokeBO<REQ> extends BatchInvokeBO {


    /**
     * batchConsumer
     */
    private List<BatchConsumerBO<REQ>> batchConsumers;



    /**
     * consumerOf
     *
     * @param batchConsumers batchConsumers
     * @return {@link BatchConsumerInvokeBO}
     */
    public static <REQ> BatchConsumerInvokeBO<REQ> of(Executor executor, List<BatchConsumerBO<REQ>> batchConsumers) {
        BatchConsumerInvokeBO<REQ> invoke = new BatchConsumerInvokeBO<>();
        invoke.setExecutor(executor);
        invoke.setBatchConsumers(batchConsumers);
        return invoke;
    }

}
