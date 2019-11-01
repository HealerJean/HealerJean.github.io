package com.healerjean.proj.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.dto.system.DepartmentDTO;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.system.RoleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName LoginUserDTO
 * @date 2019/10/16  11:21.
 * @Description
 */
@ApiModel(description = "登陆用户信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUserDTO {

    @ApiModelProperty(value = "登陆用户Id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户对应部门信息")
    private DepartmentDTO departmentDTO;

    @ApiModelProperty(value = "菜单集合")
    private List<MenuDTO> menus;


    @ApiModelProperty(value = "权限集合")
    private List<MenuDTO> permissions;

    @ApiModelProperty(value = "角色集合")
    private List<RoleDTO> roles;

}
