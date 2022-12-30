package com.healerjean.proj.service.inner.impl;

import com.healerjean.proj.service.inner.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2022/9/16  19:36.
 */
@Slf4j
@Service("aInterfaceService")
public class AInterfaceServiceImpl implements InterfaceService {

    @Override
    public void doMethod() {
        log.info("[AInterfaceServiceImpl#doMethod]");
    }
}
