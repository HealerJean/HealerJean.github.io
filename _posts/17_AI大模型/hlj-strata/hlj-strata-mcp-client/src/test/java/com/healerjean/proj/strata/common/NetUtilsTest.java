package com.healerjean.proj.strata.common;

import com.healerjean.proj.strata.utils.NetUtils;
import org.junit.jupiter.api.Test;

public class NetUtilsTest {

    @Test
    public void testGetLocalIp() {
        System.out.println(NetUtils.getLocalIp());
    }
}