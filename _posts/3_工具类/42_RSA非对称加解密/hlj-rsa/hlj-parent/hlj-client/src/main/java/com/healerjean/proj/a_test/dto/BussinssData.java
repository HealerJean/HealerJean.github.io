package com.healerjean.proj.a_test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author HealerJean
 * @ClassName BussinssData
 * @date 2019-10-28  23:07.
 * @Description
 */
@Data
public class BussinssData {

    @ApiModelProperty(value = "企业Id")
    private Long companyId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "企业编号")
    private String orgCode;

}
