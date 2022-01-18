package com.hlj.proj.business.function.impl;

import com.hlj.proj.business.function.ILogRecordService;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.LogRecord;

/**
 * @author zhangyujin
 * @date 2022/1/17  9:45 下午.
 * @description
 */
@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
    }
}