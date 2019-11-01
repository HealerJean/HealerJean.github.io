package com.healerjean.proj.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CityDTO
 * @Author HealerJean
 * @Date 2019/6/10 10:06
 * @Description 城市
 */
@Data
public class CityDTO {

    @ApiModelProperty(value = "省-编码")
    private String provinceCode;

    @ApiModelProperty(value = "省-名称")
    private String provinceName;

    @ApiModelProperty(value = "城市-编码")
    private String cityCode;

    @ApiModelProperty(value = "城市-名称")
    private String cityName;
}
