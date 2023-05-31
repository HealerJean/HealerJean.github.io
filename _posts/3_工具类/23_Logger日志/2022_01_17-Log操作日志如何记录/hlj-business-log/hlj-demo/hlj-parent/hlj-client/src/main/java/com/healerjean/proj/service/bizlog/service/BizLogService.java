package com.healerjean.proj.service.bizlog.service;

import com.healerjean.proj.service.bizlog.data.BizLogContext;

/**
 * BizLogService
 * @author zhangyujin
 * @date 2023/5/30  18:59.
 */
public interface BizLogService {

    /**
     * 业务日志执行
     */
     void bizLogExecute(BizLogContext bizLogContext);
}
