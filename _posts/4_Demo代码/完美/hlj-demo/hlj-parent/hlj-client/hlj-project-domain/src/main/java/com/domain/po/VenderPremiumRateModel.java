package com.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商家费率模型表
 * </p>
 *
 * @author example author
 * @date 2023-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VenderPremiumRateModel extends Model<VenderPremiumRateModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键标识列
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * UUID,场景_客户类型_客户Id_险种
     */
    private String uuid;

    /**
     * 客户Id,根据类型不同,含义不同,type=VENDER表示零售商家Id,type=FACTORY表示供应商ID
     */
    private String customId;

    /**
     * 客户类型,VENDER-商家,FACTORY-供应商
     */
    private String customType;

    /**
     * 业务场景,JDLS-京东零售,JDTC-京东同城
     */
    private String busScene;

    /**
     * 险种Id,4位商家险险种编码
     */
    private String insureId;

    /**
     * 出险率,-1表示无出险率数据
     */
    private BigDecimal riskRate;

    /**
     * 赔付率,-1表示无赔付率数据
     */
    private BigDecimal payoutRate;

    /**
     * 保费调整系数,默认1,不调整
     */
    private BigDecimal premiumRatio;

    /**
     * 数据状态,默认1有效
     */
    private Boolean dataStatus;

    /**
     * 数据日期yyyy-MM-dd
     */
    private String dataDt;

    /**
     * 记录创建时间
     */
    private Date createdTime;

    /**
     * 记录最后更新时间
     */
    private Date modifiedTime;


    /**
     * id
     */
    public static final String ID = "id";

    /**
     * uuid
     */
    public static final String UUID = "uuid";

    /**
     * custom_id
     */
    public static final String CUSTOM_ID = "custom_id";

    /**
     * custom_type
     */
    public static final String CUSTOM_TYPE = "custom_type";

    /**
     * bus_scene
     */
    public static final String BUS_SCENE = "bus_scene";

    /**
     * insure_id
     */
    public static final String INSURE_ID = "insure_id";

    /**
     * risk_rate
     */
    public static final String RISK_RATE = "risk_rate";

    /**
     * payout_rate
     */
    public static final String PAYOUT_RATE = "payout_rate";

    /**
     * premium_ratio
     */
    public static final String PREMIUM_RATIO = "premium_ratio";

    /**
     * data_status
     */
    public static final String DATA_STATUS = "data_status";

    /**
     * data_dt
     */
    public static final String DATA_DT = "data_dt";

    /**
     * created_time
     */
    public static final String CREATED_TIME = "created_time";

    /**
     * modified_time
     */
    public static final String MODIFIED_TIME = "modified_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
