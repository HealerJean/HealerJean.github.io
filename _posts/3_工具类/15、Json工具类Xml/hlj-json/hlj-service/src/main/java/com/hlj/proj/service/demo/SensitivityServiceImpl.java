package com.hlj.proj.service.demo;

import com.hlj.proj.api.demo.SensitivityService;
import com.hlj.proj.dto.sensitivity.SensitivityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
@Service
@Slf4j
public class SensitivityServiceImpl implements SensitivityService {

    @Override
    public SensitivityDTO getDemoDTO(SensitivityDTO demoEntity) {
        return demoEntity;
    }

}
