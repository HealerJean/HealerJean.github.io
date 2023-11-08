package com.healerjean.proj.service.bizlog.data.bo;

import lombok.Builder;
import lombok.Data;

/**
 * 解析后的模版
 *
 * @author zhangyujin
 * @date 2023/5/30  15:16
 */
@Data
@Builder
public class LogRecordBO {

    /**
     * 操作成功的的文本模版
     */
    private String successLogTemplate;
    /**
     * 操作失败的的文本模版
     */
    private String failLogTemplate;
    /**
     * 操作人
     */
    private String operatorId;
    /**
     * 业务key由 prefix + bizNo拼接而成
     */
    private String bizKey;
    /**
     * 业务编号
     */
    private String bizNo;
    /**
     * 日志分类
     */
    private String category;

    /**
     * 扩展参数，记录日志的详情数据
     */
    private String detail;

    /**
     * 记录日志的条件
     */
    private String condition;
}
