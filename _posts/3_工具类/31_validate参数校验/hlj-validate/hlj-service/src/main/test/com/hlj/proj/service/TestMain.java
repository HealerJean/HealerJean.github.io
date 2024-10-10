package com.hlj.proj.service;

import com.hlj.proj.dto.Demo.DemoDTO;
import com.hlj.proj.utils.check.CheckParamsUtils;
import org.junit.Test;

import static com.hlj.proj.utils.check.CheckParamsUtils.ALL_PARAMS;
import static com.hlj.proj.utils.check.CheckParamsUtils.ANY_PARAMS;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2024/9/30
 */
public class TestMain {

    @Test
    public void alll(){
        DemoDTO demo = new DemoDTO();
        demo.setName("healerjean");
        demo.setAge(null);
        boolean checkFlag = CheckParamsUtils.checkParams(ALL_PARAMS, demo, DemoDTO::getName, DemoDTO::getAge);
        System.out.println(checkFlag);
    }

    @Test
    public void any(){
        DemoDTO demo = new DemoDTO();
        demo.setName("healerjean");
        demo.setAge(null);
        boolean checkFlag = CheckParamsUtils.checkParams(ANY_PARAMS, demo, DemoDTO::getName, DemoDTO::getAge);
        System.out.println(checkFlag);
    }
}
