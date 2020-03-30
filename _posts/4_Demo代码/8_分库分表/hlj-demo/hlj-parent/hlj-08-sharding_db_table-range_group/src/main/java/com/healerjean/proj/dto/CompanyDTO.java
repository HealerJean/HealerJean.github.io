package com.healerjean.proj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healerjean.proj.utils.DateUtils;
import com.healerjean.proj.utils.json.JsonLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyDTO {

    @JsonSerialize(using = JsonLongSerializer.class)
    private Long id;

    private Long refUserId;

    private String name;
    private String companyNameEnglish;
    private String status;


    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = DateUtils.YYYY_MM_dd_HH_mm_ss, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonFormat(pattern = DateUtils.YYYY_MM_dd_HH_mm_ss, timezone = "GMT+8")
    private Date updateTime;
}
