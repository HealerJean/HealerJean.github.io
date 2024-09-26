package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.CountDownLatch;

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
     * 倒计时器
     */
    private CountDownLatch countDownLatch;


    /**
     * countDown
     *
     */
    public void countDown(){
        this.countDownLatch.countDown();
    }

    /**
     * await
     *
     */
    public void await(){
        try {
            this.countDownLatch.await();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
