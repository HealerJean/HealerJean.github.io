package com.healerjean.proj.code.com.healerjean.proj.template.po;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预签约记录(PreSignRecord)表实体类
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PreSignRecord extends Model<PreSignRecord> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    /**
     * id
     */
    public static final String ID = "id";

    /**
     * insurance_id
     */
    public static final String INSURANCE_ID = "insurance_id";

    /**
     * customer_code
     */
    public static final String CUSTOMER_CODE = "customer_code";

    /**
     * customer_type
     */
    public static final String CUSTOMER_TYPE = "customer_type";

    /**
     * biz_id
     */
    public static final String BIZ_ID = "biz_id";

    /**
     * type
     */
    public static final String TYPE = "type";

    /**
     * sign_channel_source
     */
    public static final String SIGN_CHANNEL_SOURCE = "sign_channel_source";

    /**
     * start_time
     */
    public static final String START_TIME = "start_time";

    /**
     * end_time
     */
    public static final String END_TIME = "end_time";

    /**
     * status
     */
    public static final String STATUS = "status";

    /**
     * sign_fail_reason
     */
    public static final String SIGN_FAIL_REASON = "sign_fail_reason";

    /**
     * biz_info
     */
    public static final String BIZ_INFO = "biz_info";

    /**
     * ext
     */
    public static final String EXT = "ext";

    /**
     * version
     */
    public static final String VERSION = "version";

    /**
     * pre_sign_time
     */
    public static final String PRE_SIGN_TIME = "pre_sign_time";

    /**
     * sign_user
     */
    public static final String SIGN_USER = "sign_user";

    /**
     * ref_sign_up_status_id
     */
    public static final String REF_SIGN_UP_STATUS_ID = "ref_sign_up_status_id";

    /**
     * operate_time
     */
    public static final String OPERATE_TIME = "operate_time";

    /**
     * operator
     */
    public static final String OPERATOR = "operator";

    /**
     * delete_flag
     */
    public static final String DELETE_FLAG = "delete_flag";

    /**
     * modified_time
     */
    public static final String MODIFIED_TIME = "modified_time";

    /**
     * created_time
     */
    public static final String CREATED_TIME = "created_time";

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

