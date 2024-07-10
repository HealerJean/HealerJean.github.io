package com.healerjean.proj.utils;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.data.bo.BusinessConsumerBO;
import com.healerjean.proj.data.bo.BusinessFunctionBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Function;

/**
 * ExceptionUtils
 *
 * @author zhangyujin
 * @date 2024/4/2
 */
@Slf4j
public class ExceptionUtils {

    /**
     * catchException
     *
     * @param function function
     * @param req req
     * @param log log
     * @param msg msg
     * @return {@link RES}
     */
    public static <REQ, RES> RES catchFunctionException(Function<REQ, RES> function, REQ req, Logger log, String msg) {
        try {
            return function.apply(req);
        } catch (Exception e) {
            log.info("[RedisCacheAspect#tryExceptionIgnore] msg:{}, req:{}", msg, req, e);
        }
        return null;
    }


    /**
     * 调用所有消费者，失败返回具体任务
     *
     * @param batchConsumers batchConsumers
     * @return {@link }
     */
    public static <R> List<BusinessConsumerBO<R>> invokeAllConsumerFailException(List<BusinessConsumerBO<R>> batchConsumers) {
        List<BusinessConsumerBO<R>> result = Lists.newArrayList();
        batchConsumers.parallelStream().forEach(batchConsumer->{
            result.add(batchConsumer);
            try {
                batchConsumer.getConsumer().accept(batchConsumer.getReq());
                batchConsumer.setInvokeFlag(true);
            } catch (Exception e) {
                batchConsumer.setInvokeFlag(false);
                batchConsumer.setException(e);
                log.info("[ExceptionUtils#invokeAllConsumerFailException] taskName:{}, req:{}", batchConsumer.getTaskName(), JSON.toJSONString(batchConsumer.getReq()), e);
            }
        });
        return result;
    }

    /**
     * 调用所有消费者，失败返回具体任务
     *
     * @param businessFunctions batchConsumers
     * @return {@link }
     */
    public static <Req, Res> List<BusinessFunctionBO<Req, Res>> invokeAllFunctionFailException(List<BusinessFunctionBO<Req, Res>> businessFunctions) {
        List<BusinessFunctionBO<Req, Res>> result = Lists.newArrayList();
        businessFunctions.parallelStream().forEach(function ->{
            result.add(function);
            try {
                function.setRes(function.getFunction().apply(function.getReq()));
                function.setInvokeFlag(true);
            } catch (Exception e) {
                function.setInvokeFlag(false);
                function.setException(e);
                log.info("[ExceptionUtils#invokeAllFunctionFailException] taskName:{}, req:{}", function.getTaskName(), JSON.toJSONString(function.getReq()), e);
            }
        });
        return result;
    }


    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllFunctionFailException(){
        BusinessFunctionBO<String, String> ironManFunction = new BusinessFunctionBO<>();
        ironManFunction.setReq("ironMan");
        ironManFunction.setTaskName("fly");
        ironManFunction.setFunction(req-> "success");

        BusinessFunctionBO<String, String> thorFunction = new BusinessFunctionBO<>();
        thorFunction.setReq("thor");
        thorFunction.setTaskName("jump");
        thorFunction.setFunction(req->{
            // int i =  1/0 ;
            return "fail";
        });
        List<BusinessFunctionBO<String, String>> list = Lists.newArrayList();
        list.add(ironManFunction);
        list.add(thorFunction);

        List<BusinessFunctionBO<String, String>> result = invokeAllFunctionFailException(list);
        log.info("result:{}", JSON.toJSONString(result));
    }


    /**
     * testInvokeAllConsumerFailException
     *
     */
    @Test
    public void testInvokeAllConsumerFailException(){
        BusinessConsumerBO<String> ironManConsumer = new BusinessConsumerBO<>();
        ironManConsumer.setReq("ironMan");
        ironManConsumer.setTaskName("fly");
        ironManConsumer.setConsumer("ironMan"::equals);

        BusinessConsumerBO<String> thorConsumer = new BusinessConsumerBO<>();
        thorConsumer.setReq("thor");
        thorConsumer.setTaskName("jump");
        thorConsumer.setConsumer(req->{
            // int i =  1/0 ;
        });
        List<BusinessConsumerBO<String>> list = Lists.newArrayList();
        list.add(ironManConsumer);
        list.add(thorConsumer);

        List<BusinessConsumerBO<String>> result = invokeAllConsumerFailException(list);
        log.info("result:{}", JSON.toJSONString(result));
    }





}
