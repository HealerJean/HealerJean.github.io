package com.hlj.proj.service.demo;

import com.hlj.proj.api.demo.DemoEntityService;
import com.hlj.proj.dto.Demo.DemoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
@Service
@Slf4j
public class DemoEntityServiceImpl implements DemoEntityService {

    @Override
    public DemoDTO getDemoDTO(DemoDTO demoEntity) {
        return demoEntity;
    }

}
