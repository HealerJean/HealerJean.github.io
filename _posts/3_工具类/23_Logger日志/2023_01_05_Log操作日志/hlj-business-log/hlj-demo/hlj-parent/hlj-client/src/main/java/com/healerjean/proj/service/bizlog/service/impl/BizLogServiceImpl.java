package com.healerjean.proj.service.bizlog.service.impl;

import com.healerjean.proj.service.bizlog.anno.LogRecordAnnotation;
import com.healerjean.proj.service.bizlog.data.BizLogContext;
import com.healerjean.proj.service.bizlog.data.bo.LogRecordBO;
import com.healerjean.proj.service.bizlog.data.po.LogRecord;
import com.healerjean.proj.service.bizlog.service.parse.LogValueParser;
import com.healerjean.proj.service.bizlog.service.BizLogService;
import com.healerjean.proj.service.bizlog.service.ILogRecordService;
import com.healerjean.proj.service.bizlog.service.IOperatorGetService;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhangyujin
 * @date 2023/5/30  18:59.
 */
@Slf4j
@Service
public class BizLogServiceImpl implements BizLogService {

    /**
     * logValueParser
     */
    @Resource
    private LogValueParser logValueParser;

    /**
     * operatorGetService
     */
    @Resource
    private IOperatorGetService operatorGetService;

    /**
     * bizLogService
     */
    @Resource
    private ILogRecordService bizLogService;

    /**
     * 业务日志执行
     * 1、获取日志对象
     * 2、获取所有的SPEL模版
     * 3、获取所有的SPEL函数结果
     * 4、日志处理
     */
    @Override
    public void bizLogExecute(BizLogContext bizLogContext) {
        // 1、获取日志对象
        LogRecordBO logRecordBo = buildLogRecordBO(bizLogContext);

        // 2、获取所有的SPEL模版
        List<String> spElTemplates = buildBeforeExecuteFunctionTemplate(logRecordBo);
        bizLogContext.setSpElTemplates(spElTemplates);

        // 3、获取所有的SPEL函数结果
        Map<String, String> functionReturnMap = logValueParser.buildFunctionResult(bizLogContext);
        bizLogContext.setFunctionReturnMap(functionReturnMap);

        // 4、日志处理
        logExecute(logRecordBo, bizLogContext);
    }


    /**
     * 记录结果
     * 1、根据切面日志结果获取成功或者失败标识模版
     * 2、获取需要解析的表达式
     * 3、解析表达式
     * 4、日志数据处理
     *
     * @param bizLogContext bizLogContext
     */
    private void logExecute(LogRecordBO logRecordBo, BizLogContext bizLogContext) {
        // 1、根据切面日志结果获取成功或者失败标识模版
        BizLogContext.MethodExecuteResult executeResult = bizLogContext.getMethodExecuteResult();
        String bizResult;
        if (executeResult.isSuccess()) {
            bizResult = logRecordBo.getSuccessLogTemplate();
        } else {
            bizResult = logRecordBo.getFailLogTemplate();
        }
        if (StringUtils.isEmpty(bizResult)) {
            return;
        }

        // 2、获取需要解析的表达式
        List<String> spElTemplates = new ArrayList<>();
        spElTemplates.add(bizResult);
        spElTemplates.add(logRecordBo.getBizKey());
        spElTemplates.add(logRecordBo.getBizNo());
        spElTemplates.add(logRecordBo.getDetail());
        if (StringUtils.isNotBlank(logRecordBo.getCondition())) {
            spElTemplates.add(logRecordBo.getCondition());
        }
        if (StringUtils.isNotBlank(logRecordBo.getOperatorId())) {
            spElTemplates.add(logRecordBo.getOperatorId());
        }
        bizLogContext.setSpElTemplates(spElTemplates);
        String runOperator = "";
        if (StringUtils.isBlank(logRecordBo.getOperatorId())) {
            runOperator = operatorGetService.getUser().getOperatorId();
            if (StringUtils.isEmpty(runOperator)) {
                throw new IllegalArgumentException("操作人获取失败");
            }
        }

        // 3、解析表达式
        Map<String, String> expressionValues = logValueParser.processTemplate(bizLogContext);


        // 4、日志数据处理
        boolean saveLogFlag = false;
        if (StringUtils.isBlank(logRecordBo.getCondition()) || StringUtils.endsWithIgnoreCase(expressionValues.get(logRecordBo.getCondition()), "true")) {
            saveLogFlag = true;
        }

        if (Boolean.FALSE.equals(saveLogFlag)) {
            log.info(" 不存储日志 logRecord:{} ", JsonUtils.toJsonString(logRecordBo));
            return;
        }
        LogRecord logRecord = LogRecord.builder()
                .bizKey(expressionValues.get(logRecordBo.getBizKey()))
                .bizNo(expressionValues.get(logRecordBo.getBizNo()))
                .operator(getRealOperatorId(logRecordBo, runOperator, expressionValues))
                .category(logRecordBo.getCategory())
                .detail(expressionValues.get(logRecordBo.getDetail()))
                .action(expressionValues.get(bizResult))
                .createTime(new Date())
                .build();

        //如果 action 为空，不记录日志
        if (StringUtils.isEmpty(logRecord.getAction())) {
            return;
        }
        bizLogService.record(logRecord);

    }


    /**
     * 获取日志对象集合
     *
     * @param bizLogContext bizLogContext
     * @return buildLogRecordBO
     */
    private LogRecordBO buildLogRecordBO(BizLogContext bizLogContext) {
        LogRecordAnnotation recordAnnotation = bizLogContext.getLogRecordAnnotation();
        if (StringUtils.isBlank(recordAnnotation.success()) && StringUtils.isBlank(recordAnnotation.fail())) {
            throw new IllegalStateException("成功或者失败模版必须有一个");
        }
        return LogRecordBO.builder()
                .successLogTemplate(recordAnnotation.success())
                .failLogTemplate(recordAnnotation.fail())
                .bizKey(recordAnnotation.prefix().concat("_").concat(recordAnnotation.bizNo()))
                .bizNo(recordAnnotation.bizNo())
                .operatorId(recordAnnotation.operator())
                .category(StringUtils.isEmpty(recordAnnotation.category()) ? recordAnnotation.prefix() : recordAnnotation.category())
                .detail(recordAnnotation.detail())
                .condition(recordAnnotation.condition())
                .build();
    }

    /**
     * getBeforeExecuteFunctionTemplate
     *
     * @param logRecordBo logRecordBo
     * @return List<String>
     */
    private List<String> buildBeforeExecuteFunctionTemplate(LogRecordBO logRecordBo) {
        List<String> templates = new ArrayList<>();
        templates.add(logRecordBo.getBizKey());
        templates.add(logRecordBo.getBizNo());
        templates.add(logRecordBo.getDetail());
        if (StringUtils.isNotBlank(logRecordBo.getCondition())) {
            templates.add(logRecordBo.getCondition());
        }
        templates.add(logRecordBo.getSuccessLogTemplate());
        return templates;
    }

    private String getRealOperatorId(LogRecordBO operation, String operatorIdFromService, Map<String, String> expressionValues) {
        return !StringUtils.isEmpty(operatorIdFromService) ? operatorIdFromService : expressionValues.get(operation.getOperatorId());
    }
}
