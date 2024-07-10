package com.healerjean.proj.code.com.healerjean.proj.template.req;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 预签约记录(PreSignRecord)QueryReq对象
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Accessors(chain = true)
@Data
public class PreSignRecordQueryReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private Long id;

    /**
     * 险种Id
     */
    private String insuranceId;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户类型
     */
    private String customerType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 预签约类型
     */
    private String type;

    /**
     * 签约渠道
     */
    private String signChannelSource;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 签约失败原因
     */
    private String signFailReason;

    /**
     * 业务信息
     */
    private String bizInfo;

    /**
     * 扩展信息
     */
    private String ext;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 预签约时间
     */
    private Date preSignTime;

    /**
     * 签约人
     */
    private String signUser;

    /**
     * 签约记录Id
     */
    private Long refSignUpStatusId;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 0 正常, 1 删除
     */
    private Integer deleteFlag;

    /**
     * 修改时间
     */
    private Date modifiedTime;

    /**
     * 创建时间
     */
    private Date createdTime;

}

