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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 获取树形结构所有部门
     * @return
     */
    @GetMapping("/tree")
    public ResponseBean getDepartmentTree() {
        log.info("部门管理------获取部门树");
        List<DepartmentDTO> departmentTree = departmentService.getDepartmentTree();
        log.info("部门管理------获取部门树成功");
        return ResponseBean.buildSuccess("获取部门树成功",  departmentTree);
    }



    /**
     * 添加部门
     * @param departmentDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseBean addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        log.info("部门管理------添加部门，部门信息：{}", departmentDTO);
        String validate = ValidateUtils.validate(departmentDTO, ValidateGroup.AddDepartment.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("添加部门参数错误，错误信息：{}", validate);
            throw new ParameterErrorException(validate);
        }
        departmentService.addDepartment(departmentDTO, UserUtils.getLoginUser());
        log.info("部门管理------添加部门成功，部门信息：{}", departmentDTO);
        return ResponseBean.buildSuccess("添加部门成功");
    }

    /**
     * 修改部门
     * @param id
     * @param departmentDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseBean updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        log.info("部门管理------修改部门，部门信息：{}", departmentDTO);
        departmentDTO.setId(id);
        String validate = ValidateUtils.validate(departmentDTO, ValidateGroup.UpdateDepartment.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("修改部门参数错误，错误信息：{}", validate);
            throw new ParameterErrorException(validate);
        }
        departmentService.updateDepartment(departmentDTO, UserUtils.getLoginUser());
        log.info("部门管理------修改部门成功，部门信息：{}", departmentDTO);
        return ResponseBean.buildSuccess("修改部门成功");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean deleteDepartment(@PathVariable Long id) {
        log.info("部门管理------删除部门，部门id：{}", id);
        if (id == null || id == 0L) {
            throw new ParameterErrorException("请选择要删除的部门");
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        departmentService.deleteDepartment(departmentDTO);
        log.info("部门管理------删除部门成功，部门id：{}", id);
        return ResponseBean.buildSuccess("删除部门成功");
    }
}
