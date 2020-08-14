package com.healerjean.proj.H02_Stream.H02_简单属性.H07_foreach;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        list.stream().forEach(System.out::print);

        // for 循环不能终止，应为是吧一个一个的消费者放进去的
        list.forEach(str -> {
            if ("张无忌".equals(str)) {
                //这里的return 相当于continue没有结束循环，而是继续下一个
                return;
            }
            System.out.println(str);
        });
    }

}
