package com.healerjean.proj.service.inner.impl;

import com.healerjean.proj.service.inner.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2022/9/16  19:37.
 */
@Slf4j
@Service("bInterfaceService")
public class BInterfaceServiceImpl implements InterfaceService {

    @Override
    public void doMethod() {
        log.info("[BInterfaceServiceImpl#doMethod]");
    }
}
