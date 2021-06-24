package com.hlj.proj.testmain;


import com.hlj.proj.service.log.LogCallTestService;
import com.hlj.proj.service.log.ThirdInvokeLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.logging.LogLevel;

/**
 * @author zhangyujin
 * @date 2021/6/24  11:20 上午.
 * @description
 */
@Slf4j
public class TestMain1_LogCall {

    @Test
    public void test(){
        LogCallTestService logCallTestService = new LogCallTestService();
        LogCallTestService.RequestDTO requestDTO = new  LogCallTestService.RequestDTO();
        requestDTO.setName("healerjean");
        requestDTO.setAge(1L);
        ThirdInvokeLogAspect.call("invokeMethod", requestDTO, () -> logCallTestService.invokeMethod(requestDTO), log);


        ThirdInvokeLogAspect.call("invokeMethod", requestDTO, () -> logCallTestService.invokeMethod(requestDTO), log, LogLevel.DEBUG);
        ThirdInvokeLogAspect.call("invokeMethod", requestDTO, () -> logCallTestService.invokeMethod(requestDTO), log, LogLevel.INFO);
        ThirdInvokeLogAspect.call("invokeMethod", requestDTO, () -> logCallTestService.invokeMethod(requestDTO), log, LogLevel.WARN);
        ThirdInvokeLogAspect.call("invokeMethod", requestDTO, () -> logCallTestService.invokeMethod(requestDTO), log, LogLevel.ERROR);
    }
}
