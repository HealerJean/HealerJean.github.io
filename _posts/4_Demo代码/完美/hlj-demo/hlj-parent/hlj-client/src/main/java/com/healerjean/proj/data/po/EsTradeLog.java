package com.healerjean.proj.data.po;

import com.healerjean.proj.common.anno.EsIndex;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单险种投保日志
 * uuid 唯一索引 policy_no + node + node_sort_id
 *
 * @author zhangyujin
 * @date 2024-03-20 15:56:50
 */
@EsIndex("trade_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class EsTradeLog extends EsBasePO {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


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


    /**
     * uuid
     */
    public static final String UUID = "uuid";

    /**
     * trace_sort_id
     */
    public static final String TRACE_SORT_ID = "trace_sort_id";

    /**
     * node_sort_id
     */
    public static final String NODE_SORT_ID = "node_sort_id";

    /**
     * insurance_id
     */
    public static final String INSURANCE_ID = "insurance_id";

    /**
     * order_id
     */
    public static final String ORDER_ID = "order_id";

    /**
     * policy_no
     */
    public static final String POLICY_NO = "policy_no";

    /**
     * custom_type
     */
    public static final String CUSTOM_TYPE = "custom_type";

    /**
     * custom_code
     */
    public static final String CUSTOM_CODE = "custom_code";

    /**
     * in_validate_time
     */
    public static final String IN_VALIDATE_TIME = "in_validate_time";

    /**
     * type
     */
    public static final String TYPE = "type";

    /**
     * node
     */
    public static final String NODE = "node";

    /**
     * biz_desc
     */
    public static final String BIZ_DESC = "biz_desc";

    /**
     * biz_data
     */
    public static final String BIZ_DATA = "biz_data";

    /**
     * modified_time
     */
    public static final String MODIFIED_TIME = "modified_time";

    /**
     * created_time
     */
    public static final String CREATED_TIME = "created_time";

}

