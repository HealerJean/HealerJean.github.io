package com.healerjean.proj.utils;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.data.bo.BatchConsumerBO;
import com.healerjean.proj.data.bo.BatchConsumerInvokeBO;
import com.healerjean.proj.data.bo.BatchFunctionBO;
import com.healerjean.proj.data.bo.BatchFunctionInvokeBO;
import com.healerjean.proj.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
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
    public static <REQ, RES> BatchFunctionBO<REQ, RES> invokeFunction(BatchFunctionBO<REQ, RES> function) {
        List<BatchFunctionBO<REQ, RES>> functions = Lists.newArrayList();
        functions.add(function);

        BatchFunctionInvokeBO<REQ, RES> batchInvoker = BatchFunctionInvokeBO.of(functions);
        invokeAllFunction(batchInvoker).await();
        return functions.get(0);
    }

    /**
     * 调用所有函数，失败返回具体任务
     *
     * @return {@link }
     */
    public static <REQ, RES> BatchFunctionInvokeBO<REQ, RES> invokeAllFunction(BatchFunctionInvokeBO<REQ, RES> batchInvoker) {
        List<BatchFunctionBO<REQ, RES>> functions = batchInvoker.getBatchFunctions();
        functions.parallelStream().forEach(function -> {
            long startTime = System.currentTimeMillis();
            BatchFunctionBO.ResBusinessBO<RES> resBusiness = function.getResBusiness();
            try {
                resBusiness.setRes(function.getFunction().apply(function.getReqBusiness().getReq()));
                resBusiness.setInvokeFlag(true);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                resBusiness.setInvokeFlag(false);
                resBusiness.setException(e);
                resBusiness.setCost(System.currentTimeMillis() - startTime);
                log.info("[ExceptionUtils#invokeAllFunctionFailException] taskName:{}, req:{}", function.getReqBusiness().getName(), JSON.toJSONString(function.getReqBusiness().getName()), e);
            }finally {
                batchInvoker.countDown();
            }
        });
        return batchInvoker;
    }



    /**
     * 调用消费者，失败返回具体任务
     *
     * @param consumer function
     */
    public static <REQ> BatchConsumerBO<REQ> invokeConsumer(BatchConsumerBO<REQ> consumer) {
        List<BatchConsumerBO<REQ>> consumers = Lists.newArrayList();
        consumers.add(consumer);
        BatchConsumerInvokeBO<REQ> batchConsumerInvoke = BatchConsumerInvokeBO.of(consumers);
        invokeAllConsumer(batchConsumerInvoke).await();
        return batchConsumerInvoke.getBatchConsumers().get(0);
    }

    /**
     * 调用所有消费者，失败返回具体任务
     *
     * @return {@link }
     */
    public static <R> BatchConsumerInvokeBO<R> invokeAllConsumer(BatchConsumerInvokeBO<R> batchConsumerInvoke) {
        List<BatchConsumerBO<R>> batchConsumers = batchConsumerInvoke.getBatchConsumers();
        batchConsumers.parallelStream().forEach(batchConsumer -> {
            long startTime = System.currentTimeMillis();
            BatchConsumerBO.ResBusinessBO resBusiness = batchConsumer.getResBusiness();
            BatchConsumerBO.ReqBusinessBO<R> reqBusiness = batchConsumer.getReqBusiness();
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
            }finally {
                batchConsumerInvoke.countDown();
            }
        });
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
        BatchFunctionBO<String, String> ironManFunction = BatchFunctionBO.instance();
        ironManFunction.getReqBusiness().setReq("ironMan").setName("fly");
        ironManFunction.setFunction(req-> "success");


        BatchFunctionBO<String, String> thorFunction = BatchFunctionBO.instance();
        thorFunction.getReqBusiness().setReq("thor").setName("jump");
        thorFunction.setFunction(req->{
            int i =  1/0 ;
            return "fail";
        });
        List<BatchFunctionBO<String, String>> list = Lists.newArrayList();
        list.add(ironManFunction);
        list.add(thorFunction);

        BatchFunctionInvokeBO<String, String> batchFunctionInvoke = BatchFunctionInvokeBO.of(list);
        invokeAllFunction(batchFunctionInvoke).await();
        log.info("result:{}", JSON.toJSONString(batchFunctionInvoke.getBatchFunctions()));
    }


    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllConsumerFailException(){
        BatchConsumerBO<String> ironManConsumer = BatchConsumerBO.instance();
        ironManConsumer.getReqBusiness().setReq("ironMan").setName("fly");
        ironManConsumer.setConsumer("ironMan"::equals);

        BatchConsumerBO<String> thorConsumer = BatchConsumerBO.instance();
        ironManConsumer.getReqBusiness().setReq("thor").setName("jump");
        thorConsumer.setConsumer(req->{
            System.out.println(1);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // int i =  1/0 ;
        });
        List<BatchConsumerBO<String>> list = Lists.newArrayList();
        list.add(ironManConsumer);
        list.add(thorConsumer);

        BatchConsumerInvokeBO<String> batchConsumerInvoke = BatchConsumerInvokeBO.of(list);
        invokeAllConsumer(batchConsumerInvoke).await();
        log.info("result:{}", JSON.toJSONString(batchConsumerInvoke.getBatchConsumers()));
    }





}
