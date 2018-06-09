package com.hlj.threadpool.ThreadStatus;

import org.junit.jupiter.api.Test;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/28  上午11:25.
 */
public class ThreadStatus {

    @Test
    public void threadStatus(){

        for(Thread.State state:Thread.State.values()){
            System.out.println(state.name());
        }
    }
}
