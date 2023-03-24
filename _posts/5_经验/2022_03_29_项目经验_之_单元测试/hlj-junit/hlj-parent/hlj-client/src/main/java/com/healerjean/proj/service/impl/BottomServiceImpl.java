package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.BottomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BottomServiceImpl
 *
 * @author zhangyujin
 * @date 2023/3/23  15:01.
 */
@Slf4j
@Service
public class BottomServiceImpl implements BottomService {

    /**
     * 底层方法
     *
     * @param name name
     * @return String
     */
    @Override
    public String bottomMethod(String name) {
        log.info("[BottomService#bottomMethod] name:{}", name);
        return "bottomMethod:" + name;
    }
}

