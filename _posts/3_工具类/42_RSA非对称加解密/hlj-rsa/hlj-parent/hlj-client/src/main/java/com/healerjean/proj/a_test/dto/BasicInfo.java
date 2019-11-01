package com.healerjean.proj.a_test.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author HealerJean
 * @ClassName BasicInfo
 * @date 2019/10/29  14:23.
 * @Description
 */
@ApiModel(description = "交易基础消息体")
@Data
public class BasicInfo {

    @ApiModelProperty(value = "交易编号")
    private String transCode;

    @ApiModelProperty(value = "交易类型")
    private String transType;

    @ApiModelProperty(value = "交易时间")
    private Date transTime;

    @ApiModelProperty(value = "签名：一般我们只对业务数据data进行签名就可以了，没必要全部签名，有些浪费时间")
    private String signedMsg;

    /** 返回代码 */
    private String retCode;
    /** 错误信息 */
    private String retMsg;

}
