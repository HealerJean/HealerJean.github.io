package com.healerjean.proj.service.service.impl;

import com.healerjean.proj.service.service.BottomService;
import com.healerjean.proj.service.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * CenterServiceImpl
 *
 * @author zhangyujin
 * @date 2023/3/23  15:00.
 */
@Slf4j
@Service
public class CenterServiceImpl implements CenterService {

    /**
     * bottomService
     */
    @Resource
    private BottomService bottomService;

    /**
     * 中间层方法
     *
     * @param name name
     * @return String
     */
    @Override
    public String centerMethod(String name) {
        log.info("[CenterService#centerMethod] name:{}", name);
        return name + bottomService.bottomMethod("centerMethod:" + name + "_");
    }
}
