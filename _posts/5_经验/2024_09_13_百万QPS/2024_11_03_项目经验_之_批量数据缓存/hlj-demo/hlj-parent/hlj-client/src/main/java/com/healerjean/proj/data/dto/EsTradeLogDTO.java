package com.healerjean.proj.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TradeLogDTO
 *
 * @author zhangyujin
 * @date 2024/4/26
 */
@Accessors(chain = true)
@Data
public class EsTradeLogDTO implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6186310839487829718L;

    /**
     * uuid 唯一索引 policy_no + node + node_sort_id
     */
    private String uuid;

    /**
     * 链路排序Id
     */
    private Integer traceSortId;

    /**
     * 链路节点排序Id
     */
    private Integer nodeSortId;

    /**
     * 险种编码
     */
    private String insuranceId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 客户类型
     */
    private String customType;

    /**
     * 客户编码
     */
    private String customCode;

    /**
     * 失效时间
     */
    private String inValidateTime;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志类型对应节点类型
     */
    private String node;

    /**
     * 描述信息
     */
    private String bizDesc;

    /**
     * 业务数据
     */
    private String bizData;

    /**
     * 修改时间
     */
    private String modifiedTime;

    /**
     * 创建时间
     */
    private String createdTime;

}