package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName DistrictDTO
 * @Author TD
 * @Date 2019/6/10 10:06
 * @Description 地区传输对象
 */
@ApiModel(description = "地区DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictDTO {

    @ApiModelProperty(value = "省-编码")
    private String provinceCode;

    @ApiModelProperty(value = "省-名称")
    private String provinceName;

    @ApiModelProperty(value = "城市-编码")
    private String cityCode;

    @ApiModelProperty(value = "城市-名称")
    private String cityName;

    @ApiModelProperty(value = "区/县-编码")
    private String districtCode;

    @ApiModelProperty(value = "区/县-名称")
    private String districtName;
}
