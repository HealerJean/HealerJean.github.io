package com.healerjean.proj.utils;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.config.ThreadPoolConfiguration;
import com.healerjean.proj.data.bo.batch.stream.BatchConsumerBO;
import com.healerjean.proj.data.bo.batch.stream.BatchConsumerInvokeBO;
import com.healerjean.proj.data.bo.batch.stream.BatchFunctionBO;
import com.healerjean.proj.data.bo.batch.stream.BatchFunctionInvokeBO;
import com.healerjean.proj.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * FunctionUtils
 *
 * @author zhangyujin
 * @date 2024/4/2
 */
@Slf4j
public class FunctionUtils {

    /**
     * 调用函数，失败返回具体任务
     *
     * @param function function
     */
    public static  BatchFunctionBO invokeFunction(Executor executor, BatchFunctionBO function) {
        List<BatchFunctionBO> functions = Lists.newArrayList();
        functions.add(function);

        BatchFunctionInvokeBO batchInvoker = BatchFunctionInvokeBO.of(executor, functions);
        invokeAllFunction(batchInvoker);
        return functions.get(0);
    }

    /**
     * 调用所有函数，失败返回具体任务
     *
     * @return {@link }
     */
    @SuppressWarnings("unchecked")
    public static  BatchFunctionInvokeBO invokeAllFunction(BatchFunctionInvokeBO batchInvoker) {
        List<BatchFunctionBO> functions = batchInvoker.getBatchFunctions();
        Executor executor = batchInvoker.getExecutor();
        CompletableFuture<BatchConsumerBO.ResBusinessBO>[] tasks = functions.stream().map(batchFunction -> CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            BatchFunctionBO.ResBusinessBO resBusiness = batchFunction.getResBusiness();
            try {
                Object req = batchFunction.getReqBusiness().getReq();
                resBusiness.setRes(batchFunction.getFunction().apply(req));
                resBusiness.setInvokeFlag(true);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                resBusiness.setInvokeFlag(false);
                resBusiness.setException(e);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
                log.info("[ExceptionUtils#invokeAllFunctionFailException] taskName:{}, req:{}", batchFunction.getReqBusiness().getName(), JSON.toJSONString(batchFunction.getReqBusiness().getName()), e);
            }
            return resBusiness;
        }, executor)).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(tasks).join();
        return batchInvoker;
    }



    /**
     * 调用消费者，失败返回具体任务
     *
     * @param consumer function
     */
    public static  BatchConsumerBO invokeConsumer(Executor executor, BatchConsumerBO consumer) {
        List<BatchConsumerBO> consumers = Lists.newArrayList();
        consumers.add(consumer);
        BatchConsumerInvokeBO batchConsumerInvoke = BatchConsumerInvokeBO.of(executor, consumers);
        invokeAllConsumer(batchConsumerInvoke);
        return batchConsumerInvoke.getBatchConsumers().get(0);
    }

    /**
     * 调用所有消费者，失败返回具体任务
     *
     * @return {@link }
     */
    @SuppressWarnings("unchecked")
    public static  BatchConsumerInvokeBO invokeAllConsumer(BatchConsumerInvokeBO batchConsumerInvoke) {
        List<BatchConsumerBO> batchConsumers = batchConsumerInvoke.getBatchConsumers();
        Executor executor = batchConsumerInvoke.getExecutor();
        CompletableFuture<BatchConsumerBO.ResBusinessBO>[] tasks = batchConsumers.stream().map(batchConsumer -> CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            BatchConsumerBO.ResBusinessBO resBusiness = batchConsumer.getResBusiness();
            BatchConsumerBO.ReqBusinessBO reqBusiness = batchConsumer.getReqBusiness();
            BatchConsumerBO.ReqBusinessBO.IdempotentBO idempotent = reqBusiness.getIdempotent();
            try {
                idempotentInvoke(batchConsumer.getConsumer(), reqBusiness.getReq(), idempotent);
                resBusiness.setInvokeFlag(true);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                resBusiness.setInvokeFlag(false);
                resBusiness.setException(e);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
                log.info("[ExceptionUtils#invokeAllConsumerFailException] taskName:{}, req:{}", reqBusiness.getName(), JSON.toJSONString(reqBusiness.getReq()), e);
            }
            return resBusiness;
        }, executor)).toArray(CompletableFuture[]::new);


        CompletableFuture.allOf(tasks).join();
        return batchConsumerInvoke;
    }


    /**
     * 幂等
     */
    public static  <R> void idempotentInvoke(Consumer<R> consumer, R r, BatchConsumerBO.ReqBusinessBO.IdempotentBO idempotent) {
        if (Objects.isNull(idempotent)) {
            consumer.accept(r);
            return;
        }
        RedisService redisService = idempotent.getRedisService();
        String uuid = idempotent.getUuid();
        String idempotentFlag = redisService.get(uuid);
        if (Objects.nonNull(idempotentFlag)) {
            return;
        }
        consumer.accept(r);
        redisService.set(uuid, "1", idempotent.getExpireSec());
    }



    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllFunctionFailException(){
        BatchFunctionBO ironManFunction = BatchFunctionBO.instance();
        ironManFunction.getReqBusiness().setReq("ironMan").setName("fly");
        ironManFunction.setFunction(req-> "success");


        BatchFunctionBO thorFunction = BatchFunctionBO.instance();
        thorFunction.getReqBusiness().setReq(123).setName("jump");
        thorFunction.setFunction(req->{
            int i =  1/0 ;
            return "fail";
        });
        List<BatchFunctionBO> list = Lists.newArrayList();
        list.add(ironManFunction);
        list.add(thorFunction);

        BatchFunctionInvokeBO batchFunctionInvoke = BatchFunctionInvokeBO.of(ThreadPoolConfiguration.THREAD_POOL_TASK_EXECUTOR, list);
        invokeAllFunction(batchFunctionInvoke);
        log.info("result:{}", JSON.toJSONString(batchFunctionInvoke.getBatchFunctions()));
    }


    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllConsumerFailException(){
        List<BatchConsumerBO> list = Lists.newArrayList();

        BatchConsumerBO ironManConsumer = BatchConsumerBO.instance();
        ironManConsumer.getReqBusiness().setReq("ironMan").setName("fly");
        ironManConsumer.setConsumer("ironMan"::equals);

        for (int i = 0; i < 1000; i++) {
            BatchConsumerBO thorConsumer = BatchConsumerBO.instance();
            int finalI = i;
            thorConsumer.setConsumer(req->{
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // int i =  1/0 ;
                log.info("finalI:{}", finalI);
            });
            thorConsumer.getReqBusiness().setReq("thor" + i).setName("jump" + i);
            list.add(thorConsumer);
        }

        list.add(ironManConsumer);
        BatchConsumerInvokeBO batchConsumerInvoke = BatchConsumerInvokeBO.of(ThreadPoolConfiguration.THREAD_POOL_TASK_EXECUTOR, list);
        invokeAllConsumer(batchConsumerInvoke);
        log.info("result:{}", JSON.toJSONString(batchConsumerInvoke.getBatchConsumers()));
    }




}
