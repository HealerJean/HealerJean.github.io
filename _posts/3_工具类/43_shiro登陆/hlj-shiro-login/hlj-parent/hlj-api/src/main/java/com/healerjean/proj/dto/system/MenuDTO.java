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
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName MenuDTO
 * @Author HealerJean
 * @Date 2019/1/9 18:11
 * @Description 菜单传输对象
 */
@ApiModel(description = "菜单DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO implements Serializable {

    @ApiModelProperty(value = "主键Id")
    @NotNull(message = "菜单id不能为空", groups = {ValidateGroup.UpdateMenu.class})
    private Long id;

    @ApiModelProperty(value = "系统编号")
    @NotBlank(message = "系统编号不能为空", groups = {ValidateGroup.AddMenu.class, ValidateGroup.UpdateMenu.class})
    @Length(message = "系统编号长度不能大于32", max = 32)
    private String systemCode;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空", groups = {ValidateGroup.AddMenu.class, ValidateGroup.UpdateMenu.class})
    @Length(message = "菜单名称长度不能大于64", max = 64)
    private String menuName;

    @ApiModelProperty(value = "相对路径")
    @Length(message = "调用地址长度不能大于255", max = 255)
    private String url;

    @ApiModelProperty(value = "调用方法")
    @Length(message = "调用方法长度不能大于32", max = 32)
    private String method;

    @ApiModelProperty(value = "父菜单id")
    @NotNull(message = "父菜单不能为空", groups = {ValidateGroup.AddMenu.class, ValidateGroup.UpdateMenu.class})
    private Long pid;

    @ApiModelProperty(value = "父菜单链")
    private String pchain;

    @ApiModelProperty(value = "菜单描述")
    @Length(message = "菜单描述长度不能大于255", max = 255)
    private String descrption;

    @ApiModelProperty(value = "菜单序列")
    private Integer sort;

    @ApiModelProperty(value = "菜单图标")
    @Length(message = "菜单图标长度不能大于255", max = 255)
    private String icon;

    @ApiModelProperty(value = "前端菜单标识（前端菜单唯一标识）")
    @Length(message = "前端菜单标识长度不能大于64", max = 64)
    private String frontKey;

    @ApiModelProperty(value = "是否需要权限拦截，10：需要，99：不需要")
    @Length(message = "是否需要权限拦截长度不能大于2", max = 2)
    private String isPermission;

    @ApiModelProperty(value = "菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单")
    @Length(message = "菜单类型长度不能大于2", max = 2)
    private String menuType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否返回树形结构")
    private Boolean toTree;

    @ApiModelProperty(value = "子菜单")
    private List<MenuDTO> submenus;

    @ApiModelProperty(value = "子菜单")
    private List<MenuDTO> children;

    @ApiModelProperty(value = "分组后的菜单")
    List<MenuDTO> frontMenus;

    @ApiModelProperty(value = "分组后的菜单")
    List<MenuDTO> backMenus;

    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "分组后的菜单")
    private List<Long> front;

    @ApiModelProperty(value = "分组后的菜单")
    private List<Long> back;
}
