package com.healerjean.proj;

import com.healerjean.proj.utils.ThreadPoolUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * TomcatLauncher
 *
 * @author zhangyujin
 * @date 2023/6/14  15:30
 */
@EnableTransactionManagement
@SpringBootApplication
public class TomcatLauncher {


    public static void main(String[] args) {
        SpringApplication.run(TomcatLauncher.class, args);

        for (int i = 0; true; i++) {
            int finalI = i;
            ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR.execute(()-> System.out.println("默认线程池执行" + finalI));
            if (ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR.getPoolSize() >= 6000){
                break;
            }
        }
        System.out.println("核心线程数" + ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR.getPoolSize());
    }

}
