package com.healerjean.proj.controller.system;

import com.healerjean.proj.api.system.DepartmentService;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.DepartmentDTO;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.util.UserUtils;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName DepartmentController
 * @Author DYB
 * @Date 2019/6/4 13:50
 * @Description 部门管理
 * @Version V1.0
 */
@Api(description = "系统管理-部门管理")
@Slf4j
@RestController
@RequestMapping("/hlj/sys/departments")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;


    @ApiOperation(value = "部门管理-获取树形结构所有部门",
            notes = "部门管理-获取树形结构所有部门",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DepartmentDTO.class)
    @GetMapping("/tree")
    public ResponseBean getDepartmentTree() {
        log.info("部门管理--------获取树形结构所有部门");
        List<DepartmentDTO> departmentTree = departmentService.getDepartmentTree();
        return ResponseBean.buildSuccess("获取树形结构所有部门成功", departmentTree);
    }


    @ApiOperation(value = "部门管理-部门添加",
            notes = "部门管理-部门添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DepartmentDTO.class)
    @PostMapping("/add")
    public ResponseBean addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        log.info("部门管理--------部门添加，部门信息：{}", departmentDTO);
        String validate = ValidateUtils.validate(departmentDTO, ValidateGroup.AddDepartment.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("添加部门参数错误，错误信息：{}", validate);
            throw new ParameterErrorException(validate);
        }
        departmentService.addDepartment(departmentDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("部门添加成功");
    }


    @ApiOperation(value = "部门管理-部门修改",
            notes = "部门管理-部门修改",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DepartmentDTO.class)
    @PutMapping("/{id}")
    public ResponseBean updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        log.info("部门管理--------部门修改，部门信息：{}", departmentDTO);
        departmentDTO.setId(id);
        String validate = ValidateUtils.validate(departmentDTO, ValidateGroup.UpdateDepartment.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("修改部门参数错误，错误信息：{}", validate);
            throw new ParameterErrorException(validate);
        }
        departmentService.updateDepartment(departmentDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("部门修改成功");
    }


    @ApiOperation(value = "部门管理-部门删除",
            notes = "部门管理-部门删除",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DepartmentDTO.class)
    @DeleteMapping("/{id}")
    public ResponseBean deleteDepartment(@PathVariable Long id) {
        log.info("部门管理--------部门删除，部门id：{}", id);
        if (id == null || id == 0L) {
            throw new ParameterErrorException("请选择要删除的部门");
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        departmentService.deleteDepartment(departmentDTO);
        return ResponseBean.buildSuccess("部门删除成功");
    }
}
