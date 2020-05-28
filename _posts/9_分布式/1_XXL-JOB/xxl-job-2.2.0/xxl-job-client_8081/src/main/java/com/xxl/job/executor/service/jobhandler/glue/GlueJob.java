package com.xxl.job.executor.service.jobhandler.glue;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;

public class GlueJob  extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("GlueJob 请求参数：{}", param);
        return ReturnT.SUCCESS;
    }
}
