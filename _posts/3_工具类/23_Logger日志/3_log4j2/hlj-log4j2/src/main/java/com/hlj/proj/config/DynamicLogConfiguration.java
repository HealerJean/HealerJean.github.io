package com.hlj.proj.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * DynamicLogConfiguration
 *
 * @author zhangyujin
 * @date 2023/11/24
 */
@Slf4j
@Data
@Configuration
public class DynamicLogConfiguration {

    /**
     * 入参是否打印 举例：com.hlj.proj.controller.Log4j2Controller.log4j2
     */
    private Map<String, Boolean> reqPrintFlag;

    /**
     * 出参是否打印 举例：com.hlj.proj.controller.Log4j2Controller.log4j2
     */
    private Map<String, Boolean> resPrintFlag;

    /**
     * getReqPrintFlag
     */
    public Map<String, Boolean> getReqPrintFlag() {
        if (CollectionUtils.isEmpty(reqPrintFlag)) {
            return new HashMap<>(16);
        }
        return reqPrintFlag;
    }

    /**
     * getResPrintFlag
     */
    public Map<String, Boolean> getResPrintFlag() {
        if (CollectionUtils.isEmpty(resPrintFlag)) {
            return new HashMap<>(16);
        }
        return resPrintFlag;
    }
}
