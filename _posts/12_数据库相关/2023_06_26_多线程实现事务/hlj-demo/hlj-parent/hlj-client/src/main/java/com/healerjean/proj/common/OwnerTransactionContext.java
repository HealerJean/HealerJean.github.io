package com.healerjean.proj.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * TransactionHolder
 *
 * @author zhangyujin
 * @date 2023/6/26$  11:59$
 */
@Accessors(chain = true)
@Data
public class OwnerTransactionContext {

    /**
     * 倒计数器
     */
    private CountDownLatch ownerCountDownLatch;

    /**
     * 错误信息
     */
    private Vector<Throwable> errorMsgs;

    public static OwnerTransactionContext initInstance(int taskCount) {
        return new OwnerTransactionContext()
                .setErrorMsgs(new Vector<>())
                .setOwnerCountDownLatch(new CountDownLatch(taskCount));

    }
}
