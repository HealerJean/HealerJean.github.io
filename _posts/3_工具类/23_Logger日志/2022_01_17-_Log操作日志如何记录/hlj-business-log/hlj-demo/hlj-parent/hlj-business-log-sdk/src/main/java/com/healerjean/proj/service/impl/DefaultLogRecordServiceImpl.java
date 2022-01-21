package com.healerjean.proj.service.impl;

import com.google.common.collect.Lists;
import com.healerjean.proj.beans.LogRecord;
import com.healerjean.proj.service.ILogRecordService;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author muzhantong
 * create on 2020/4/29 4:34 下午
 */
@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", JsonUtils.toJsonString(logRecord));
    }

    @Override
    public List<LogRecord> queryLog(String bizKey) {
        return Lists.newArrayList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo) {
        return Lists.newArrayList();
    }
}
