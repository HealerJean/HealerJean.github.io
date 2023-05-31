package com.healerjean.proj.service.bizlog.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * BusinessLogService
 * @author zhangyujin
 * @date 2023/5/31  21:14.
 */
@Slf4j
@Service
public class BusinessLogService {

    /**
     * BUSINESS_LOG
     */
    private static final Logger BUSINESS_LOG = LoggerFactory.getLogger("businessLog");

    @Test
    public void test(){
        BUSINESS_LOG.info("业务日志");
        log.debug("INFO 系统日志");
        log.info("INFO 系统日志");
        log.error("ERROR 系统日志");
    }

}
