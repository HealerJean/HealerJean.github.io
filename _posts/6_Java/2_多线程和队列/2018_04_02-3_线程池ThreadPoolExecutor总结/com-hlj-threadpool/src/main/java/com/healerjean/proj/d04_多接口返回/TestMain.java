package com.healerjean.proj.d04_多接口返回;

import com.google.common.collect.Lists;
import com.healerjean.proj.d04_多接口返回.utils.CompletableFutureTimeout;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author zhangyujin
 * @date 2021/12/14  4:26 下午.
 * @description
 */
@Slf4j
public class TestMain {

    /**
     * 无返回值的异步任务  CompletableFuture.runAsync
     */
    @Test
    public void test1() throws Exception {
        ExecutorService executer = Executors.newFixedThreadPool(10);

        //1.无返回值的异步任务 runAsync()
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getName());
        }, executer);

        Thread.sleep(5000L);
    }


    /**
     * 有返回结果的异步执行 CompletableFuture.supplyAsync
     * 1、completableFuture.whenComplete
     * 第一个参数是结果
     * 第二个参数是异常，他可以感知异常，无法返回默认数据
     * */
    @Test
    public void test_whenComplete() throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(10);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getId());
            int i = 1 / 0;
            return "HealerJean";
        }, executor);


        log.info("==============");
        CompletableFuture<String> completableFuture2 = completableFuture.whenComplete((r, e) -> {
            log.info("[completableFuture.whenComplete] result:{} ", r, e);
        });

        Thread.sleep(5000L);
    }


    /**
     * 有返回结果的异步执行 CompletableFuture.supplyAsync
     * 2、completableFuture.exceptionally
     * 只有一个参数是异常类型，他可以感知异常，同时返回默认数据
     */
    @Test
    public void test_exceptionally() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getId());
            int i = 1 / 0;
            return "HealerJean";
        }, executor);


        CompletableFuture<String> exceptionally = completableFuture.exceptionally(e -> {
            log.error("[completableFuture.exceptionally] error", e);
            return "exceptionally";
        });

        Thread.sleep(5000L);
    }

    /**
     * 有返回结果的异步执行 CompletableFuture.supplyAsync
     * 3、completableFuture.handle
     * 既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合
     */
    @Test
    public void test2() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("currentThread:{}", Thread.currentThread().getId());
            int i = 1 / 0;
            return "HealerJean";
        }, service);


        log.info("==============");
        CompletableFuture<String> handle = completableFuture.handle((r, e) -> {
            if (r != null) {
                log.error("[completableFuture.handle] result {}", r);
                return r;
            }
            if (e != null) {
                log.error("[completableFuture.handle] error", e);
                return "error";
            }
            return "";
        });

        Thread.sleep(10000L);
    }


    /**
     * 串行任务
     * 1、thenRunAsync() 无入参，无返回值（线程池执行）
     */
    @Test
    public void test_thenRunAsync() throws InterruptedException {
        log.info("[test]  currentThread:{}", Thread.currentThread().getId());
        // currentThread:1
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] currentThread:{}", Thread.currentThread().getId());
            // completableFuture.supplyAsync] currentThread:11
            return "HealerJean";
        }, executor);

        // 1、thenRunAsync()  无入参，无返回值（线程池执行）
        completableFuture.thenRunAsync(() -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenRunAsync] currentThread:{}, cost:{}", Thread.currentThread().getId(), cost);
            // [completableFuture.thenRunAsync] currentThread:12, cost:84
        }, executor);

        Thread.sleep(5000L);
    }


    /**
     * 串行任务
     * 2、thenAccept() 有入参，无返回值，不传线程池(main线程)
     */
    @Test
    public void test_thenAccept(){
        log.info("[test]  currentThread:{}", Thread.currentThread().getId());
        // currentThread:1

        long start = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(10);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] currentThread:{}", Thread.currentThread().getId());
            // [completableFuture.supplyAsync] currentThread:11
            return "HealerJean";
        }, service);

        // 2、thenAccept() 有入参，无返回值，不传线程池(main线程)
        completableFuture.thenAccept((r) -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenAccept]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
            // [completableFuture.thenAccept]  currentThread:1, result:HealerJean, cost:76
        });
    }

    /**
     * 线程串行化
     * 3、thenApply() 有入参，有返回值，不传线程池(main线程)
     */
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        log.info("[test]  currentThread:{}", Thread.currentThread().getId());
        // currentThread:1

        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[completableFuture.supplyAsync] currentThread:{}", Thread.currentThread().getId());
            // [completableFuture.supplyAsync] currentThread:11
            return "HealerJean";
        }, executor);


        // 3、thenApply() 有入参，有返回值，不传线程池(main线程)
        completableFuture.thenApply((r) -> {
            long cost = System.currentTimeMillis() - start;
            log.info("[completableFuture.thenApply]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
            // [completableFuture.thenApply]  currentThread:1, result:HealerJean, cost:74
            return "thenAccept";
        });

        Thread.sleep(5000L);
    }


    /**
     * 4、异步，两任务组合 ：两个异步任务都完成，第三个任务才开始执行
     */
    @Test
    public void test4() throws InterruptedException {
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


        Thread.sleep(5000L);
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
     * 6、输出最先完成的n个任务
     */
    @Test
    public void test6() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
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

        int n = 2;
        CountDownLatch latch = new CountDownLatch(n);
        List<CompletableFuture<String>> results = Lists.newArrayList();
        CompletableFuture<String>[] completableFutures = new CompletableFuture[]{task1, task2, task3};
        // 对每个future添加完成时的回调
        for (CompletableFuture<String> future : completableFutures) {
            future.whenComplete((r, e) -> {
                synchronized (results) {
                    if (results.size() < n) {
                        latch.countDown();
                        results.add(future);
                    }
                }
            });
        }

        latch.await();
        for (CompletableFuture<String> future: results){
            log.info("[test] task finish  result:{}", future.get());
        }
        Thread.sleep(5000L);
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

            int i = 1/0;
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task3 end ");
            return "task3 success";
        });

        CompletableFuture<String> completableFuture1 = CompletableFutureTimeout.completeOnTimeout(task1, "Exception", "TimeOutException", 2, TimeUnit.MILLISECONDS);
        CompletableFuture<Integer> completableFuture2 = CompletableFutureTimeout.completeOnTimeout(task2, 0, -1,1, TimeUnit.SECONDS);
        CompletableFuture<String> completableFuture3 = CompletableFutureTimeout.completeOnTimeout(task3, "Exception","TimeOutException", 1, TimeUnit.MILLISECONDS);

        String result1 = completableFuture1.get();
        Integer result2 = completableFuture2.get();
        String result3 = completableFuture3.get();
        Long cost = System.currentTimeMillis() - start;
        log.info("result1: {}, cost:{}", result1, cost);
        log.info("result2: {}, cost:{}", result2, cost);
        log.info("result3: {}, cost:{}", result3, cost);
        Thread.sleep(500000L);
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数：只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(100);
        // 缓存队列：用来缓冲执行任务的队列
        taskExecutor.setQueueCapacity(500);
        // 线程池维护线程所允许的空闲时间：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(60);
        // threadPoolTaskExecutor
        taskExecutor.setThreadNamePrefix("threadPoolTaskExecutor");
        // 调度器shutdown被调用时等待当前被调度的任务完成：用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间:如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        log.info("Executor - threadPoolTaskExecutor injected!");
        return taskExecutor;
    }

    @Test
    public void testTimeOut() throws InterruptedException, ExecutionException {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolTaskExecutor();

        CompletionService<String> completionService = new ExecutorCompletionService<>(threadPoolTaskExecutor);
        List<Future<String>> futures = Arrays.asList(
                completionService.submit(() -> {
                    Thread.sleep(5000);
                    return "5000";
                }),
                completionService.submit(() -> {
                    Thread.sleep(1000);
                    return "1000";
                }),
                completionService.submit(() -> {
                    Thread.sleep(6000);
                    return "6000";
                })
        );

        int timeOut = 2500;
        for (int i = 0; i < futures.size(); i++) {
            Future<String> future = completionService.poll(timeOut, TimeUnit.MILLISECONDS);
            if (future == null){
                System.out.println("获取任务超时");
                continue;
            }
            System.out.println(future.get());
        }
    }


    @Test
    public void testTimeOut2() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolTaskExecutor();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("task1 start ");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task1 end ");
            return "task1 success";
        }, threadPoolTaskExecutor);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info("task3 start ");
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task3 end ");
            return "task3 success";
        }, threadPoolTaskExecutor);


        // 等待所有任务完成，或者直到50毫秒后超时
        try {
            CompletableFuture.allOf(new CompletableFuture[]{task1,task3}).get(50, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 这里处理超时异常
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            // 这里处理其他可能的异常
            e.printStackTrace();
        }
    }

    /**
     * 获取线程池结果
     *
     * @param allCheckFuture allCheckFuture
     * @return MarginEnum.InsuredFailEnum
     */
    public void allCheckFutureResult( Map<Integer, String> result, CompletableFuture<String>[] allCheckFuture) {
        for (int i = 0; i < allCheckFuture.length; i++) {
            CompletableFuture<String>  completableFuture =  allCheckFuture[i];
            try {
                result.put(i, completableFuture.get());
            } catch (Exception e) {
                log.error("[MarginService#vendorInsureCheck] ERROR", e);
            }
        }
    }


}
