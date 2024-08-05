package com.healerjean.proj.utils;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.data.bo.BusinessConsumerBO;
import com.healerjean.proj.data.bo.BusinessFunctionBO;
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
    public static <REQ, RES> BusinessFunctionBO<REQ, RES> invokeFunction(BusinessFunctionBO<REQ, RES> function) {
        List<BusinessFunctionBO<REQ, RES>> functions = Lists.newArrayList();
        functions.add(function);

        return invokeAllFunction(functions).get(0);
    }

    /**
     * 调用所有函数，失败返回具体任务
     *
     * @param functions batchConsumers
     * @return {@link }
     */
    public static <REQ, RES> List<BusinessFunctionBO<REQ, RES>> invokeAllFunction(List<BusinessFunctionBO<REQ, RES>> functions) {
        functions.parallelStream().forEach(function -> {
            BusinessFunctionBO.ResBusinessBO<RES> resBusiness = function.getResBusiness();
            try {
                resBusiness.setRes(function.getFunction().apply(function.getReqBusiness().getReq()));
                resBusiness.setInvokeFlag(true);
            } catch (Exception e) {
                resBusiness.setInvokeFlag(false);
                resBusiness.setException(e);
                log.info("[ExceptionUtils#invokeAllFunctionFailException] taskName:{}, req:{}", function.getReqBusiness().getName(), JSON.toJSONString(function.getReqBusiness().getName()), e);
            }
        });
        return functions;
    }



    /**
     * 调用消费者，失败返回具体任务
     *
     * @param consumer function
     */
    public static <REQ> BusinessConsumerBO<REQ> invokeConsumer(BusinessConsumerBO<REQ> consumer) {
        List<BusinessConsumerBO<REQ>> consumers = Lists.newArrayList();
        consumers.add(consumer);
        return invokeAllConsumer(consumers).get(0);
    }

    /**
     * 调用所有消费者，失败返回具体任务
     *
     * @param batchConsumers batchConsumers
     * @return {@link }
     */
    public static <R> List<BusinessConsumerBO<R>> invokeAllConsumer(List<BusinessConsumerBO<R>> batchConsumers) {
        batchConsumers.parallelStream().forEach(batchConsumer -> {
            BusinessConsumerBO.ResBusinessBO resBusiness = batchConsumer.getResBusiness();
            BusinessConsumerBO.ReqBusinessBO<R> reqBusiness = batchConsumer.getReqBusiness();
            BusinessConsumerBO.ReqBusinessBO.IdempotentBO idempotent = reqBusiness.getIdempotent();
            try {
                idempotentInvoke(batchConsumer.getConsumer(), reqBusiness.getReq(), idempotent);
                resBusiness.setInvokeFlag(true);
            } catch (Exception e) {
                resBusiness.setInvokeFlag(false);
                resBusiness.setException(e);
                log.info("[ExceptionUtils#invokeAllConsumerFailException] taskName:{}, req:{}", reqBusiness.getName(), JSON.toJSONString(reqBusiness.getReq()), e);
            }
        });
        return batchConsumers;
    }


    /**
     * 幂等
     */
    public static  <R> void idempotentInvoke(Consumer<R> consumer, R r, BusinessConsumerBO.ReqBusinessBO.IdempotentBO idempotent) {
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
        BusinessFunctionBO<String, String> ironManFunction = BusinessFunctionBO.instance();
        ironManFunction.getReqBusiness().setReq("ironMan").setName("fly");
        ironManFunction.setFunction(req-> "success");


        BusinessFunctionBO<String, String> thorFunction = BusinessFunctionBO.instance();
        thorFunction.getReqBusiness().setReq("thor").setName("jump");
        thorFunction.setFunction(req->{
            int i =  1/0 ;
            return "fail";
        });
        List<BusinessFunctionBO<String, String>> list = Lists.newArrayList();
        list.add(ironManFunction);
        list.add(thorFunction);

        List<BusinessFunctionBO<String, String>> result = invokeAllFunction(list);
        log.info("result:{}", JSON.toJSONString(result));
    }


    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllConsumerFailException(){
        BusinessConsumerBO<String> ironManConsumer = BusinessConsumerBO.instance();
        ironManConsumer.getReqBusiness().setReq("ironMan").setName("fly");
        ironManConsumer.setConsumer("ironMan"::equals);

        BusinessConsumerBO<String> thorConsumer = BusinessConsumerBO.instance();
        ironManConsumer.getReqBusiness().setReq("thor").setName("jump");
        thorConsumer.setConsumer(req->{
            // int i =  1/0 ;
        });
        List<BusinessConsumerBO<String>> list = Lists.newArrayList();
        list.add(ironManConsumer);
        list.add(thorConsumer);

        List<BusinessConsumerBO<String>> result = invokeAllConsumer(list);
        log.info("result:{}", JSON.toJSONString(result));
    }





}
