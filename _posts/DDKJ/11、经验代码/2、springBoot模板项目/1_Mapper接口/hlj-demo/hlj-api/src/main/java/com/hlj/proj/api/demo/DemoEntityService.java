package com.hlj.proj.api.demo;

import com.hlj.proj.common.page.PageDTO;
import com.hlj.proj.dto.Demo.DemoDTO;

import java.util.List;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface DemoEntityService {

    DemoDTO addDemoEntity(DemoDTO demoEntity);

    List<DemoDTO> queryDemoList(DemoDTO demoDTO);

    PageDTO<DemoDTO> queryDemoPage(DemoDTO demoDTO);

    DemoDTO findById(Long id);

}
