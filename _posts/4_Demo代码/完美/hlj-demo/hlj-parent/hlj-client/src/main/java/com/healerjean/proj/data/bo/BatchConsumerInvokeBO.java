package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * BatchInvokeBO
 *
 * @author zhangyujin
 * @date 2024/9/24
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BatchConsumerInvokeBO<T> extends BatchInvokeBO {


    /**
     * batchConsumer
     */
    private List<BatchConsumerBO<T>> batchConsumers;


    /**
     * consumerOf
     *
     * @param batchConsumers batchConsumers
     * @return {@link BatchConsumerInvokeBO}
     */
    public static <T> BatchConsumerInvokeBO<T> of(List<BatchConsumerBO<T>> batchConsumers) {
        BatchConsumerInvokeBO<T> invoke = new BatchConsumerInvokeBO<>();
        invoke.setBatchConsumers(batchConsumers);
        invoke.setCountDownLatch(new CountDownLatch(batchConsumers.size()));
        return invoke;
    }

}
