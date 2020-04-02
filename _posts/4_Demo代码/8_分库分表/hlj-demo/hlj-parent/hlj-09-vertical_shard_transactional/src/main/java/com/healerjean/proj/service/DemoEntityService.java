package com.healerjean.proj.service;


import com.healerjean.proj.dto.DemoDTO;

import java.util.List;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface DemoEntityService {


    DemoDTO insert(DemoDTO demoEntity);

    DemoDTO findById(Long id);

    List<DemoDTO> list();

}
