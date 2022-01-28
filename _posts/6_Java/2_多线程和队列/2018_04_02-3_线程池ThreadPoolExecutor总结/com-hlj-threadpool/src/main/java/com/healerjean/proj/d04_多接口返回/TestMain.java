package com.healerjean.proj.d04_多接口返回;

import com.healerjean.proj.d04_多接口返回.utils.CompletableFutureTimeout;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zhangyujin
 * @date 2021/12/14  4:26 下午.
 * @description
 */
@Slf4j
public class TestMain {

    /**
     * 1、无返回值的异步任务  CompletableFuture.runAsync
     */
    @Test
    public void test1() {
        ExecutorService service = Executors.newFixedThreadPool(10);

        //1.无返回值的异步任务 runAsync()
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getId());
        }, service);

    }


    /**
     * 2.有返回值异步任务 CompletableFuture.supplyAsync
     */
    @Test
    public void test2() {
        ExecutorService service = Executors.newFixedThreadPool(10);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getId());
            int i = 1 / 0;
            return "HealerJean";
        }, service);


        log.info("==============");
        // 1、whenComplete
        // 第一个参数是结果
        // 第二个参数是异常，他可以感知异常，无法返回默认数据
        completableFuture.whenComplete((r, e) -> {
            log.info("[completableFuture.whenComplete] result:{} ", r, e);
        });

        log.info("==============");
        // 2、exceptionally
        // 只有一个参数是异常类型，他可以感知异常，同时返回默认数据10
        completableFuture.exceptionally(e -> {
            log.error("[completableFuture.exceptionally] error", e);
            return "exceptionally";
        });

        log.info("==============");
        // 3、handler
        // 既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合
        completableFuture.handle((r, e) -> {
            if (r != null) {
                log.error("[completableFuture.handle] ", r);
                return r;
            }
            if (e != null) {
                log.error("[completableFuture.handle] error", r);
                return "error";
            }
            return "";
        });

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 3.线程串行化
     */
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        log.info("[test]  currentThread:{}", Thread.currentThread().getId());

        long start = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(10);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] currentThread:{}", Thread.currentThread().getId());

            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "HealerJean";
        }, service);

        // 1、thenRunAsync() 无入参，无返回值 、线程池执行
        completableFuture.thenRunAsync(() -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenRunAsync] currentThread:{}, cost:{}", Thread.currentThread().getId(), cost);
        }, service);


        // 2、thenAccept() 有入参，无返回值，不传线程池 （和CompletableFuture.supplyAsync 用的一个线程池，非异步）
        completableFuture.thenAccept((r) -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenAccept]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
        });


        String result = completableFuture.get();
        log.info("[stringCompletableFuture.get()]  result:{}", result);

        // 3、thenApply() 有入参，有返回值，不传线程池 main线程
        CompletableFuture<String> stringCompletableFuture = completableFuture.thenApply((r) -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenApply]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
            return "thenAccept";
        });
        result = stringCompletableFuture.get();
        log.info("[stringCompletableFuture.get()]  result:{}", result);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 4、异步，两任务组合 ：两个异步任务都完成，第三个任务才开始执行
     */
    @Test
    public void test4() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        //定义两个任务
        //任务一
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
            return "task_1";
        }, service);

        //任务二
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
            return "task_2";
        }, service);


        // 1、runAfterBothAsync：无传入值、无返回值
        task1.runAfterBothAsync(task2, () -> {
            log.info("[completableFuture.runAfterBothAsync] task3 currentThread:{}", Thread.currentThread().getId());
        }, service);

        // 2、thenAcceptBothAsync：有传入值、无返回值
        task1.thenAcceptBothAsync(task2, (x, y) -> {
            log.info("[completableFuture.thenAcceptBothAsync] task3 currentThread:{}, result1:{}, result2:{}", Thread.currentThread().getId(), x, y);
        }, service);

        // 2、thenCombineAsync：有传入值、有返回值
        task1.thenCombineAsync(task2, (x, y) -> {
            log.info("[completableFuture.thenCombineAsync] task3 currentThread:{}, result1:{}, result2:{}", Thread.currentThread().getId(), x, y);
            return "task3";
        }, service);


        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 4.3、当一个任务失败后快速返回
     */
    @Test
    public void test4_3() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try {
                int i = 1 / 0;
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
            return "task_1";
        }, service);
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
            return "task_2";
        }, service);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
            return "task_3";
        }, service);


        CompletableFuture[] completableFutures = new CompletableFuture[]{task1, task2, task3};
        try {
            CompletableFuture.allOf(completableFutures).join();
        } catch (Exception e) {
            log.info("[CompletableFuture.allOf] error", e);
        }
        Long cost = System.currentTimeMillis() - start;
        log.info("[test] task finish cost:{}", cost);

    }

    /**
     * 5、等待所有任务都结束
     */
    @Test
    public void test5() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_1";
        }, service);
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_2";
        }, service);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_3";
        }, service);


        // 第一种方式：CompletableFuture.allOf
        CompletableFuture[] completableFutures = new CompletableFuture[]{task1, task2, task3};
        CompletableFuture.allOf(completableFutures).join();
        Long cost = System.currentTimeMillis() - start;
        log.info("[test] task finish cost:{}", cost);

        // 第二种方式：completableFuture.get()
        // for (CompletableFuture completableFuture : completableFutures){
        //     Object result = completableFuture.get();
        //     Long cost = System.currentTimeMillis() - start;
        //     log.info("[task]  cost:{},  result:{}", cost, result);
        // }
    }


    /**
     * 6、一个任务完成则结束
     */
    @Test
    public void test6() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_1";
        }, service);
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_2";
        }, service);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "task_3";
        }, service);


        CompletableFuture[] completableFutures = new CompletableFuture[]{task1, task2, task3};
        Object result = CompletableFuture.anyOf(completableFutures).join();
        Long cost = System.currentTimeMillis() - start;
        log.info("[test] task finish cost:{}, result:{}", cost, result);
    }


    @Test
    public void test() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("task1 start ");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task1 end ");
            return "task1 success";
        });

        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            log.info("task2 start ");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task2 end ");
            return 100;
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info("task3 start ");

            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task3 end ");
            return "task3 success";
        });

        CompletableFuture<String> completableFuture1 = CompletableFutureTimeout.completeOnTimeout(task1, "InterTimeOut", 2, TimeUnit.SECONDS);
        CompletableFuture<Integer> completableFuture2 = CompletableFutureTimeout.completeOnTimeout(task2, 0, 2, TimeUnit.SECONDS);
        CompletableFuture<String> completableFuture3 = CompletableFutureTimeout.completeOnTimeout(task3, "InterTimeOut", 2, TimeUnit.SECONDS);

        String result1 = completableFuture1.get();
        Integer result2 = completableFuture2.get();
        String result3 = completableFuture3.get();
        Long cost = System.currentTimeMillis() - start;
        log.info("result1: {}, cost:{}", result1, cost);
        log.info("result2: {}, cost:{}", result2, cost);
        log.info("result3: {}, cost:{}", result3, cost);
        Thread.sleep(500000L);
    }


}
