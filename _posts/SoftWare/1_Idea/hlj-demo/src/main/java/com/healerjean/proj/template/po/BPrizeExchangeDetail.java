package com.healerjean.proj.template.po;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)表实体类
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BPrizeExchangeDetail extends Model<BPrizeExchangeDetail> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    /**
     * id
     */
    public static final String ID = "id";

    /**
     * created_date
     */
    public static final String CREATED_DATE = "created_date";

    /**
     * modified_date
     */
    public static final String MODIFIED_DATE = "modified_date";

    /**
     * benefits_id
     */
    public static final String BENEFITS_ID = "benefits_id";

    /**
     * vender_id
     */
    public static final String VENDER_ID = "vender_id";

    /**
     * exchange_time
     */
    public static final String EXCHANGE_TIME = "exchange_time";

    /**
     * benefits_name
     */
    public static final String BENEFITS_NAME = "benefits_name";

    /**
     * cost_score
     */
    public static final String COST_SCORE = "cost_score";

    /**
     * exchange
     */
    public static final String EXCHANGE = "exchange";

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

