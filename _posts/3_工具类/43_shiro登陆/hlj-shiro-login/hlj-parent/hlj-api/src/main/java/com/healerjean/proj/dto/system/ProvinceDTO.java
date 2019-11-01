package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName ProvinceDTO
 * @Author TD
 * @Date 2019/6/10 10:06
 * @Description 省份传输对象
 */
@ApiModel(description = "省份DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvinceDTO {

    /** 省-编码 */
    private String provinceCode;
    /** 省-名称 */
    private String provinceName;

}
