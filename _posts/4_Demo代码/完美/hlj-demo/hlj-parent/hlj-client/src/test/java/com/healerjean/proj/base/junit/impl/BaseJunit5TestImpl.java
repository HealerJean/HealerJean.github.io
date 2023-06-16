package com.healerjean.proj.base.junit.impl;

import com.healerjean.proj.base.junit.BaseJunit5Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * BaseJunit5TestImpl
 *
 * @author zhangyujin
 * @date 2023/6/15  21:47.
 */
@Slf4j
public class BaseJunit5TestImpl extends BaseJunit5Test {

    @DisplayName("test")
    @Test
    public void test(){
        log.info("[BaseJunit5TestImpl#test]");
    }
}
