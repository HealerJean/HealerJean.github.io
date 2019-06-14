package com.hlj.proj.utils;

import com.hlj.proj.data.pojo.demo.DemoEntity;
import com.hlj.proj.dto.Demo.DemoDTO;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {

    public static DemoDTO demoToDTO(DemoEntity demoEntity){
        DemoDTO result = new DemoDTO() ;
        if(demoEntity !=null){
            result.setId(demoEntity.getId());
            result.setName(demoEntity.getName());
            result.setAge(demoEntity.getAge());
            result.setCdate(demoEntity.getCdate());
            result.setUdate(demoEntity.getUdate());
        }
        return result ;
    }
}
