package com.healerjean.proj.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName AService
 * @date 2020-11-16  20:35.
 * @Description
 */
@Slf4j
@Service
public class AService {


    public AService(BService bService){
        log.info(bService.toString());
    }
}
