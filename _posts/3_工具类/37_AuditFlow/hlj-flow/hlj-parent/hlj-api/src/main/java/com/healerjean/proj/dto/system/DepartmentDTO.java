package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DepartmentDTO
 * @Author DYB
 * @Date 2019/6/4 10:17
 * @Description
 * @Version V1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDTO implements Serializable {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "部门描述")
    private String departmentDesc;

    @ApiModelProperty(value = "父级部门id")
    private Long pid;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "子部门")
    private List<DepartmentDTO> childDepartment;
}
