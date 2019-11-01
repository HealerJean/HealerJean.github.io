package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
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

    /**
     * 省-编码
     */
    private String provinceCode;
    /**
     * 省-名称
     */
    private String provinceName;
    /**
     * 城市-编码
     */
    private String cityCode;
    /**
     * 城市-名称
     */
    private String cityName;
    /**
     * 区/县-编码
     */
    private String districtCode;
    /**
     * 区/县-名称
     */
    private String districtName;
}
