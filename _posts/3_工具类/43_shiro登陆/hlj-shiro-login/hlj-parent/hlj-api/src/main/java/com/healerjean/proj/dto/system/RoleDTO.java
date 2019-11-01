package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName RoleDTO
 * @date 2019/10/18  12:37.
 * @Description
 */
@ApiModel(description = "角色DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "角色id不能为空", groups = ValidateGroup.UpdateRole.class)
    private Long id;
    private List<Long> ids;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = {ValidateGroup.AddRole.class, ValidateGroup.UpdateRole.class})
    @Length(message = "角色名称长度不能大于64", max = 64)
    private String roleName;

    @ApiModelProperty(value = "系统编号")
    @NotBlank(message = "系统编号不能为空", groups = {ValidateGroup.AddRole.class, ValidateGroup.UpdateRole.class})
    @Length(message = "系统编号长度不能大于32", max = 32)
    private String systemCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "描述")
    @Length(message = "角色描述长度不能大于255", max = 255)
    private String description;

    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "不采取分页")
    private Boolean isPage;

    @ApiModelProperty(value = "分组后的菜单")
    List<MenuDTO> frontMenus;

    @ApiModelProperty(value = "分组后的菜单")
    List<MenuDTO> backMenus;
}
