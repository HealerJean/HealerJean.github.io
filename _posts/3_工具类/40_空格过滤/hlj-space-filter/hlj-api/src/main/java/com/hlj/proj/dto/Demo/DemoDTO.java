package com.hlj.proj.dto.Demo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoDTO {

    @ApiModelProperty(value = "demo 主键", hidden = true)
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;
    //
    // private List<String> listString;
    //
    // private List<SpaceDTO> spaceDTOS;
}
