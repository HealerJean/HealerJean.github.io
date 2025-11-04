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
public class BatchConsumerInvokeBO extends BatchInvokeBO {


    /**
     * batchConsumer
     */
    private List<BatchConsumerBO> batchConsumers;



    /**
     * consumerOf
     *
     * @param batchConsumers batchConsumers
     * @return {@link BatchConsumerInvokeBO}
     */
    public static  BatchConsumerInvokeBO of(Executor executor, List<BatchConsumerBO> batchConsumers) {
        BatchConsumerInvokeBO invoke = new BatchConsumerInvokeBO();
        invoke.setExecutor(executor);
        invoke.setBatchConsumers(batchConsumers);
        return invoke;
    }

}
