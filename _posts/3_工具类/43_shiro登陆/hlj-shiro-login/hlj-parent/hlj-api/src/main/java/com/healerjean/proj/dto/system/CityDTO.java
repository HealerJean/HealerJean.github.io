package com.healerjean.proj.dto.system;

import lombok.Data;

/**
 * @ClassName CityDTO
 * @Author TD
 * @Date 2019/6/10 10:06
 * @Description 城市
 */
@Data
public class CityDTO {

    /** 省-编码 */
    private String provinceCode;
    /** 省-名称 */
    private String provinceName;
    /** 城市-编码 */
    private String cityCode;
    /** 城市-名称 */
    private String cityName;
}
