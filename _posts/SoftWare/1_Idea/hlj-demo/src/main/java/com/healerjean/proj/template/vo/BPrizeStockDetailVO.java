package com.healerjean.proj.template.vo;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)VO对象
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Accessors(chain = true)
@Data
public class BPrizeStockDetailVO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 修改时间
     */
    private Date modifiedDate;

    /**
     * 权益主表id
     */
    private String benefitsId;

    /**
     * 变更类型 1 追加 2 减少
     */
    private Integer changeType;

    /**
     * 变更数量
     */
    private Integer changeNum;

    /**
     * 变更后数量
     */
    private Integer changeAfterNum;

    /**
     * 操作人
     */
    private String operator;

}

