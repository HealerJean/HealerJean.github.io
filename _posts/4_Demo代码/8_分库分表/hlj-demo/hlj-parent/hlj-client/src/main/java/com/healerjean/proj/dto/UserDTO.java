package com.healerjean.proj.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.dto.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

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
public class UserDTO  {

    @ApiModelProperty(value = "主键", hidden = true)
    private Long id;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空", groups = ValidateGroup.HealerJean.class)
    private String name;

    @ApiModelProperty(value = "城市")
    private String city;

}
