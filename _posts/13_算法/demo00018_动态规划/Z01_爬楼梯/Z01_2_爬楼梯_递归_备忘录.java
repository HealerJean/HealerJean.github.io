package com.hlj.arith.demo00018_动态规划.Z01_爬楼梯;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName Z01_爬楼梯
 * @date 2020/1/19  11:40.
 * @Description
 */
public class Z01_2_爬楼梯_递归_备忘录 {


    @Test
    public void method2() {
        Map<Integer, Integer> map = new HashMap<>();
        System.out.println(f2(10, map));
    }

    public int f2(Integer n, Map<Integer, Integer> map) {
        //第1级台阶有1中走法
        if (n == 1) {
            return 1;
        }

        //第2级台阶有两种走法
        if (n == 2) {
            return 2;
        }

        Integer value = map.get(n);
        if (value == null) {
            //其他台阶的走法是 如下
            value = f2(n - 1, map) + f2(n - 2, map);
            map.put(n, value);
            return value;
        }
        return value;
    }



}
