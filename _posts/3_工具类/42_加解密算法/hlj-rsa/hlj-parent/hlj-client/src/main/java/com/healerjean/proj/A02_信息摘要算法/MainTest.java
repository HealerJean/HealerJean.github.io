package com.healerjean.proj.A02_信息摘要算法;

import com.healerjean.proj.A02_信息摘要算法.A01_MD5.MD5Util;
import org.junit.Test;

import java.text.MessageFormat;

/**
 * @author HealerJean
 * @date 2020/12/11  14:02.
 * @description
 */
public class MainTest {

    String string = "healerjean";


    @Test
    public void md5Test(){
        System.out.println(MessageFormat.format("原文：{0}", new String[]{string}));

        String encrypt = MD5Util.encode(string);
        System.out.println(MessageFormat.format("MD5密文：{0}", new String[]{encrypt}));
    }
}
