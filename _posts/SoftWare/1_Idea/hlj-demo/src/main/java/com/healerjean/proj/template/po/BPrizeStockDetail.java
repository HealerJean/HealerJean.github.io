package com.healerjean.proj.template.po;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)表实体类
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BPrizeStockDetail extends Model<BPrizeStockDetail> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * change_type
     */
    public static final String CHANGE_TYPE = "change_type";

    /**
     * change_num
     */
    public static final String CHANGE_NUM = "change_num";

    /**
     * change_after_num
     */
    public static final String CHANGE_AFTER_NUM = "change_after_num";

    /**
     * operator
     */
    public static final String OPERATOR = "operator";

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

