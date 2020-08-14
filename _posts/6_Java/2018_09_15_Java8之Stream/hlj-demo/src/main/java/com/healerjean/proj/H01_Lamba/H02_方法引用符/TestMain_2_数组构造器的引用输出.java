package com.healerjean.proj.H01_Lamba.H02_方法引用符;

import com.healerjean.proj.H01_Lamba.H02_方法引用符.inter.ArrayBuilerInterface;
import org.junit.Test;


public class TestMain_2_数组构造器的引用输出 {

    @Test
    public void test() {
        method(s -> new int[s]);

        method(int[]::new);
    }

    public void method(ArrayBuilerInterface builerInterface){
        int [] nums = builerInterface.build(10);
    }
}
