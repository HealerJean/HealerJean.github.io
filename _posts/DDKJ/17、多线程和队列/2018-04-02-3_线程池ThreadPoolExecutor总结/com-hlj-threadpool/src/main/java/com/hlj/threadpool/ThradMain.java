package com.hlj.threadpool;

import com.hlj.threadpool.thread.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/2  下午5:01.
 */
@Slf4j
public class ThradMain {

    public static void main(String[] args) {

        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils();
        threadPoolUtils.execute(() ->{ //这里的参数必须是一个接口

            System.out.println(Thread.currentThread().getId());

        });


    }
}
