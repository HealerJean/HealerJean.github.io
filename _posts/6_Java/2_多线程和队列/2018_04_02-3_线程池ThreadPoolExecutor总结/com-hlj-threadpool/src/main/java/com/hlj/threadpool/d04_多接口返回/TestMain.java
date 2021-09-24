package com.hlj.threadpool.d04_多接口返回;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangyujin
 * @date 2021/9/16  4:28 下午.
 * @description
 */
public class TestMain {


    /**
     *
     */
    @Test
    public void test() {
        ExecutorService service = Executors.newFixedThreadPool(10);

        //1.无返回值的异步任务 runAsync()
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
        }, service);



        //2.有返回值异步任务 supplyAsync()
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
            return i;
        }, service).whenComplete((r, e) -> {
            // whenComplete第一个参数是结果，第二个参数是异常，他可以感知异常，无法返回默认数据
            System.out.println("执行完毕，结果是---" + r + "异常是----" + e);
        }).exceptionally(u -> {
            // exceptionally只有一个参数是异常类型，他可以感知异常，同时返回默认数据10
            return 10;
            // handler既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合
        }).handle((r, e) -> {
            if (r != null) {
                return r;
            }
            if (e != null) {
                return 0;
            }
            return 0;
        });
    }


    @Test
    public void test2(){
        /**
         * 3.线程串行化（把后边的线程和前边的串起来）
         */

        //thenRunAsync()无返回值
        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
            return i;
        }, service).thenRunAsync(() -> {
            System.out.println("thenRunAsync，不可接受传来的值，自己无返回值的串行化---");
        }, service);

        //thenAccept(x)
        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
            return i;
        }, service).thenAccept((r) -> {
            System.out.println("thenAccept可接受传来的值，自己无返回值的串行化---");
        });

        //thenApply(x)
        CompletableFuture<Integer> voidCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程号为***" + Thread.currentThread().getId());
            int i = 5;
            System.out.println("---------" + i);
            return i;
        }, service).thenApply((r) -> {
            System.out.println("thenApply可接受传来的值，自己有返回值的串行化---");
            return  10;
        });
    }



    @Test
    public void test3(){
        在这里插入代码片 /**
         * 异步，两任务组合 ：两个任务都完成，第三个任务才开始执行
         */

        //定义两个任务
        //任务一
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            int i = 5;
            System.out.println("任务一开始执行" + i);
            return i;
        }, service);

        //任务二
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            int i = 10;
            System.out.println("任务二开始执行" + i);
            return i;
        }, service);

        //要求：任务一、二都完成后才执行任务三
//      runAfterBothAsync：无传入值、无返回值
        task1.runAfterBothAsync(task2,()->{
            System.out.println("任务三开始执行-runAfterBothAsync：无传入值、无返回值  ");
        },service);

//      thenAcceptBothAsync：有传入值、无返回值
        task1.thenAcceptBothAsync(task2,(x,y)->{
            System.out.println("任务三开始执行-runAfterBothAsync：无传入值、无返回值  task1的结果是x ,task2的结果是y");
        },service);


//      thenCombineAsync：有传入值、有返回值
        task1.thenCombineAsync(task2,(x,y)->{
            System.out.println("任务三开始执行-runAfterBothAsync：无传入值、无返回值  task1的结果是x ,task2的结果是y,task3返回hello");
            return "hello";
        },service);



        /**
         * 异步，两任务组合 ：两个任务都完成其中一个完成，第三个任务才开始执行
         */

        /**
         * runAfterEither 无传入值、无返回值
         * acceptEither 有传入值、无返回值
         * applyToEither 有传入值、有返回值
         * 代码同上！
         */

    }
}
