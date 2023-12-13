package com.healerjean.proj.template.dto;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)DTO对象
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:28
 */
@Accessors(chain = true)
@Data
public class BPrizeExchangeDetailDTO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 修改时间
     */
    private Date modifiedDate;

    /**
     * 主表ID
     */
    private String benefitsId;

    /**
     * 商家ID
     */
    private String venderId;

    /**
     * 兑换时间
     */
    private Date exchangeTime;

    /**
     * 权益ID
     */
    private String benefitsName;

    /**
     * 花费积分
     */
    private Integer costScore;

    /**
     * 是否完成积分扣减 1 是 0否
     */
    private Integer exchange;

}

