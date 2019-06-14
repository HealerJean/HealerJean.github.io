package com.hlj.proj.dto.Demo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName DemoDTO
 * @date 2019/6/13  20:02.
 * @Description
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoDTO {

    @ApiModelProperty(value = "demo 主键",hidden = true)
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Long age ;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date cdate;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date udate;

}
