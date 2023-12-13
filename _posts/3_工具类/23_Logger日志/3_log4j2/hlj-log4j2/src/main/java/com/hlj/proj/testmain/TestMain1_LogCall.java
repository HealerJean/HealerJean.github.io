package com.hlj.proj.testmain;


import com.hlj.proj.service.LogCallTestService;
import com.hlj.proj.service.InvokeUtils;
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
    public void test() {
        LogCallTestService logCallTestService = new LogCallTestService();
        LogCallTestService.RequestDTO requestDTO = new LogCallTestService.RequestDTO();
        requestDTO.setName("healerjean");
        requestDTO.setAge(1L);
        LogCallTestService.ResponseDTO res = InvokeUtils.call("invokeMethod", requestDTO, logCallTestService::invokeMethod, log);

        res = InvokeUtils.call("invokeMethod", requestDTO, logCallTestService::invokeMethod, log, LogLevel.DEBUG);
        res = InvokeUtils.call("invokeMethod", requestDTO, logCallTestService::invokeMethod, log, LogLevel.INFO);
        res = InvokeUtils.call("invokeMethod", requestDTO, logCallTestService::invokeMethod, log, LogLevel.WARN);
        res = InvokeUtils.call("invokeMethod", requestDTO, logCallTestService::invokeMethod, log, LogLevel.ERROR);
    }
}
