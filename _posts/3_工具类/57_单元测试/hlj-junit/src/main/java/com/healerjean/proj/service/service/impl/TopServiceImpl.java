package com.healerjean.proj.service.service.impl;

import com.healerjean.proj.service.service.CenterService;
import com.healerjean.proj.service.service.TopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangyujin
 * @date 2023/3/23  17:13.
 */
@Slf4j
@Service
public class TopServiceImpl implements TopService {

    @Resource
    private CenterService centerService;

    @Override
    public String topMethod(String name) {
        log.info("[TopService#topMethod] name:{}", name);
        return name +  centerService.centerMethod("topMethod:" + name + "_");
    }
}
