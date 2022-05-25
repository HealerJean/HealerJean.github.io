package com.healerjean.proj.d04_多接口返回.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * java8中CompletableFuture异步处理超时的方法
 *
 * Java 8 的 CompletableFuture 并没有 timeout 机制，虽然可以在 get 的时候指定 timeout，是一个同步堵塞的操作。怎样让 timeout 也是异步的呢？Java 8 内有内建的机
 *  制支持，一般的实现方案是启动一个 ScheduledThreadpoolExecutor 线程在 timeout 时间后直接调用 CompletableFuture.completeExceptionally(new TimeoutException())，
 *  然后用acceptEither() 或者 applyToEither 看是先计算完成还是先超时：
 *
 *  在 java 9 引入了 orTimeout 和 completeOnTimeOut 两个方法支持 异步 timeout 机制：
 *
 * public CompletableFuture orTimeout(long timeout, TimeUnit unit) : completes the CompletableFuture with a TimeoutException after the specified timeout has elapsed.
 * public CompletableFuture completeOnTimeout(T value, long timeout, TimeUnit unit) : provides a default value in the case that the CompletableFuture pipeline times out.
 * 内部实现上跟我们上面的实现方案是一模一样的，只是现在不需要自己实现了。
 *
 * 实际上hystrix等熔断的框架，其实现线程Timeout之后就关闭线程，也是基于同样的道理，所以我们可以看到hystrix中会有一个Timer Thread
 *
 */
@Slf4j
public class CompletableFutureTimeout {

    static final class Delayer {
        static ScheduledFuture<?> delay(Runnable command, long delay,
                                        TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureDelayScheduler");
                return t;
            }
        }

        static final ScheduledThreadPoolExecutor delayer;

        // 注意，这里使用一个线程就可以搞定 因为这个线程并不真的执行请求 而是仅仅抛出一个异常
        static {
            (delayer = new ScheduledThreadPoolExecutor(
                    1, new CompletableFutureTimeout.Delayer.DaemonThreadFactory())).
                    setRemoveOnCancelPolicy(true);
        }
    }

    static final class FutureTimeOutException extends RuntimeException {

        public FutureTimeOutException() {
        }
    }

    static class ExceptionUtils {
        public static Throwable extractRealException(Throwable throwable) {
            //这里判断异常类型是否为CompletionException、ExecutionException，如果是则进行提取，否则直接返回。
            if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
                if (throwable.getCause() != null) {
                    return throwable.getCause();
                }
            }
            return throwable;
        }
    }
    /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
     */
    public static  <T> CompletableFuture<T> completeOnTimeout( CompletableFuture<T> future,T t, T to, long timeout, TimeUnit unit) {
        final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
        return future.applyToEither(timeoutFuture, Function.identity()).exceptionally(throwable -> {

            if (ExceptionUtils.extractRealException(throwable) instanceof FutureTimeOutException){
                return to;
            }
            return t;
        });
    }


    private static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
        CompletableFuture<T> result = new CompletableFuture<T>();
        CompletableFutureTimeout.Delayer.delayer.schedule(() -> result.completeExceptionally(new FutureTimeOutException()), timeout, unit);
        return result;
    }


}