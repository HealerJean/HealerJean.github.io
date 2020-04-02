package com.healerjean.proj.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healerjean.proj.common.dto.ValidateGroup;
import com.healerjean.proj.utils.DateUtils;
import com.healerjean.proj.utils.json.JsonLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName UserDTO
 * @date 2019/6/13  20:02.
 * @Description
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @ApiModelProperty(value = "主键", hidden = true)
    @JsonSerialize(using = JsonLongSerializer.class )
    private Long id;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空", groups = ValidateGroup.HealerJean.class)
    private String name;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "状态", hidden = true)
    private String status;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = DateUtils.YYYY_MM_dd_HH_mm_ss, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonFormat(pattern = DateUtils.YYYY_MM_dd_HH_mm_ss, timezone = "GMT+8")
    private Date updateTime;
}
