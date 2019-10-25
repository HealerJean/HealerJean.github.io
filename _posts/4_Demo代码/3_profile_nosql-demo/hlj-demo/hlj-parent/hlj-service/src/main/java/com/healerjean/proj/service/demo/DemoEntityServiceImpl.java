package com.healerjean.proj.service.demo;

import com.healerjean.proj.api.demo.DemoEntityService;
import com.healerjean.proj.dto.Demo.DemoDTO;
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
    public DemoDTO getMmethod(DemoDTO demoEntity) {
        return demoEntity;
    }

}
