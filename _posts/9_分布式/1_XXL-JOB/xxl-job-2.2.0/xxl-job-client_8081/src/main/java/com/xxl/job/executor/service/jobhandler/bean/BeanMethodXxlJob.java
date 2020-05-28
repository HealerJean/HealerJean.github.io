package com.xxl.job.executor.service.jobhandler.bean;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 */
@Component
@Slf4j
public class BeanMethodXxlJob {


    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        log.info("任务【demoJobHandler】开始执行, 执行事件：{}，请求参数：{}", DateUtil.formatDateTime(new Date()), param);
        //任务调度器日志
        XxlJobLogger.log("XXL-JOB, 请求参数：{}", param);
        return ReturnT.SUCCESS;
    }

    /**
     * 2、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
     */
    @XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
    public ReturnT<String> demoJobHandler2(String param) throws Exception {
        log.info("任务【demoJobHandler2】开始执行, 执行事件：{}，请求参数：{}", DateUtil.formatDateTime(new Date()), param);

        //任务调度器日志
        XxlJobLogger.log("XXL-JOB, hello World.");
        return ReturnT.SUCCESS;
    }

    public void init() {
        log.info("任务【demoJobHandler2】开始执行 init");
    }

    public void destroy() {
        log.info("任务【demoJobHandler2】开始执行 destroy");
    }


}
