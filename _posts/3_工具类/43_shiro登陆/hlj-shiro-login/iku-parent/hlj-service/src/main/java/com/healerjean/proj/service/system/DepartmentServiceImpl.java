package com.healerjean.proj.service.system;

import com.healerjean.proj.api.system.DepartmentService;
import com.healerjean.proj.data.manager.system.SysDepartmentManager;
import com.healerjean.proj.data.manager.system.SysUserDepartmentRefManager;
import com.healerjean.proj.data.pojo.system.SysDepartment;
import com.healerjean.proj.data.pojo.system.SysDepartmentQuery;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRef;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRefQuery;
import com.healerjean.proj.dto.system.DepartmentDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName DepartmentServiceImpl
 * @Author DYB
 * @Date 2019/6/4 9:51
 * @Description 部门管理实现类
 * @Version V1.0
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private SysDepartmentManager sysDepartmentManager;
    @Autowired
    private SysUserDepartmentRefManager sysUserUserDepartmentRefManager;

    /**
     * 获取部门树形结构
     * @return
     */
    @Override
    public List<DepartmentDTO> getDepartmentTree() {
        SysDepartmentQuery query = new SysDepartmentQuery();
        query.setStatus(StatusEnum.生效.code);
        List<SysDepartment> departments = sysDepartmentManager.queryList(query);
        return BeanUtils.departmentListToDTOsTree(departments);
    }

    /**
     * 添加部门
     * @param departmentDTO
     * @return
     */
    @Override
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO, LoginUserDTO loginUserDTO) {
        SysDepartmentQuery query = new SysDepartmentQuery();
        query.setName(departmentDTO.getDepartmentName());
        query.setStatus(StatusEnum.生效.code);
        SysDepartment department = sysDepartmentManager.findByQueryContion(query);
        if (department != null) {
            throw new BusinessException(ResponseEnum.部门名称已经存在);
        }
        if (departmentDTO.getPid() != null && departmentDTO.getPid() != 0L) {
            department = sysDepartmentManager.findById(departmentDTO.getPid());
            if (department == null) {
                throw new BusinessException(ResponseEnum.父级部门不存在);
            }
        }
        department = BeanUtils.departmentDtoToPojo(departmentDTO);
        department.setCreateUser(loginUserDTO.getUserId());
        department.setCreateName(loginUserDTO.getRealName());
        sysDepartmentManager.insertSelective(department);
        departmentDTO.setId(department.getId());
        return departmentDTO;
    }

    /**
     * 修改部门（不能修改部门父级）
     * @param departmentDTO
     * @return
     */
    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, LoginUserDTO loginUserDTO) {
        SysDepartmentQuery query = new SysDepartmentQuery();
        query.setName(departmentDTO.getDepartmentName());
        query.setStatus(StatusEnum.生效.code);
        SysDepartment department = sysDepartmentManager.findByQueryContion(query);
        if (department != null && !department.getId().equals(departmentDTO.getId())) {
            throw new BusinessException(ResponseEnum.部门名称已经存在);
        }
        department = BeanUtils.departmentDtoToPojo(departmentDTO);
        department.setPid(null);
        department.setUpdateUser(loginUserDTO.getUserId());
        department.setUpdateName(loginUserDTO.getRealName());
        int i = sysDepartmentManager.updateSelective(department);
        if (i < 1) {
            throw new BusinessException(ResponseEnum.部门名称不存在);
        }
        return departmentDTO;
    }

    /**
     * 删除部门（有子部门不可删除）
     * @param departmentDTO
     */
    @Override
    public void deleteDepartment(DepartmentDTO departmentDTO) {
        SysDepartmentQuery query = new SysDepartmentQuery();
        query.setPid(departmentDTO.getId());
        query.setStatus(StatusEnum.生效.code);
        SysDepartment department = sysDepartmentManager.findByQueryContion(query);
        if (department != null) {
            throw new BusinessException(ResponseEnum.部门存在子部门_不可删除);
        }

        SysUserDepartmentRefQuery refQuery = new SysUserDepartmentRefQuery();
        refQuery.setRefDepartmentId(departmentDTO.getId());
        refQuery.setStatus(StatusEnum.生效.code);
        List<SysUserDepartmentRef> refUserDepartments = sysUserUserDepartmentRefManager.queryList(refQuery);
        if (refUserDepartments != null && !refUserDepartments.isEmpty()) {
            throw new BusinessException(ResponseEnum.部门存在用户_不可删除);
        }

        int i = sysDepartmentManager.deleteById(departmentDTO.getId());
        if (i < 1) {
            throw new BusinessException(ResponseEnum.部门名称不存在);
        }
    }
}

