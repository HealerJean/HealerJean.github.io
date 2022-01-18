package com.hlj.proj.business.function;

import java.util.logging.LogRecord;

/**
 * @author zhangyujin
 * @date 2022/1/17  9:45 下午.
 * @description
 */
public interface ILogRecordService {
    /**
     * 保存 log
     *
     * @param logRecord 日志实体
     */
    void record(LogRecord logRecord);

}
