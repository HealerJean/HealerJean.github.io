package com.healerjean.proj.utils;

import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.pojo.DemoEntity;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {



    public static DemoEntity dtoToDemo(DemoDTO demoDTO) {
        DemoEntity result = new DemoEntity();
        result.setId(demoDTO.getId());
        result.setName(demoDTO.getName());
        result.setPhone(demoDTO.getPhone());
        result.setEmail(demoDTO.getEmail());
        result.setAge(demoDTO.getAge());
        result.setStatus(demoDTO.getStatus());
        result.setCreateUser(demoDTO.getCreateUser());
        result.setCreateName(demoDTO.getCreateName());
        result.setCreateTime(demoDTO.getCreateTime());
        result.setUpdateUser(demoDTO.getUpdateUser());
        result.setUpdateName(demoDTO.getUpdateName());
        result.setUpdateTime(demoDTO.getUpdateTime());

        return result;
    }

    public static DemoDTO demoToDTO(DemoEntity demoEntity) {
        DemoDTO dto = new DemoDTO();
        if (demoEntity != null) {
            dto.setId(demoEntity.getId());
            dto.setName(demoEntity.getName());
            dto.setAge(demoEntity.getAge());
            dto.setPhone(demoEntity.getPhone());
            dto.setEmail(demoEntity.getEmail());
            dto.setStatus(demoEntity.getStatus());
            dto.setCreateUser(demoEntity.getCreateUser());
            dto.setCreateName(demoEntity.getCreateName());
            dto.setCreateTime(demoEntity.getCreateTime());
            dto.setUpdateUser(demoEntity.getUpdateUser());
            dto.setUpdateName(demoEntity.getUpdateName());
            dto.setUpdateTime(demoEntity.getUpdateTime());
            dto.setPage(null);
            dto.setPageSize(null);
            dto.setPageNo(null);
        }
        return dto;
    }
}
