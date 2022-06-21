package com.healerjean.proj.qlexpress;

import com.google.common.collect.Lists;
import com.healerjean.proj.qlexpress.dto.UserDTO;
import com.healerjean.proj.qlexpress.operator.OperatorDeparmentBelong;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/21  17:16.
 */
@Slf4j
public class TestMain {


    /**
     * 1、自定义function
     * @throws Exception 抛出异常
     */
    @Test
    public void test_function() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        Map<String, Object> context = new HashMap<>();
        context.put(OperatorDeparmentBelong.SYSTEM_VARIABLE_DEPARTMENT, Lists.newArrayList("产品部", "研发部"));

        String express = "部门归属('商务部')";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_function] 商务部 是否归属于部门:{}", JsonUtils.toJsonString(execute));

        express = "部门归属('研发部')";
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_function] 研发部 是否归属于部门:{}", JsonUtils.toJsonString(execute));
    }

    /**
     * 2、内置函数
     */
    @Test
    public void test_innner_function() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        Map<String, Object> context = new HashMap<>();
        String express = "最大(1,2,5,4)";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_innner_function] (1,2,5,4) 最大的是:{}", JsonUtils.toJsonString(execute));

    }

    /**
     * 3、验证 addFunctionOfClassMethod
     * @throws Exception 抛出异常
     */
    @Test
    public void test_addFunctionOfClassMethod() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("数字", -3);
        String express = "取绝对值(数字)";
        ExpressManager expressManager = new ExpressManager();
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test] 取绝对值:{}", JsonUtils.toJsonString(execute));
    }

    /**
     * 3、验证addOperatorWithAlias
     */
    @Test
    public void test_addOperatorWithAlias() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("喜欢", 2);

        String express = "如果(喜欢 > 1) 则  {return 结婚;} 否则 {return 分手;}";
        ExpressManager expressManager = new ExpressManager();
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addOperatorWithAlias] 恋爱状态:{}", JsonUtils.toJsonString(execute));
    }

    /**
     *
     */
    @Test
    public void test_addMacro() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("HealerJean");
        Map<String, Object> context = new HashMap<>();
        context.put("userInfo", userDTO);
        context.put("login", true);

        String express = "用户是否注册";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", userDTO.getName(), JsonUtils.toJsonString(execute));

        userDTO.setName("zhang");
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", userDTO.getName(), JsonUtils.toJsonString(execute));

        userDTO.setName("liu");
        context.put("login", false);
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", userDTO.getName(), JsonUtils.toJsonString(execute));
    }

}
