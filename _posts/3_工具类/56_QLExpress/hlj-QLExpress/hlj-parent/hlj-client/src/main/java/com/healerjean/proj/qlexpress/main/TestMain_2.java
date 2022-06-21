package com.healerjean.proj.qlexpress.main;

import com.healerjean.proj.qlexpress.main.operator.OperatorContextPut;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.instruction.op.OperatorBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/6/17  19:49.
 */
public class TestMain_2 {

    /**
     * 1、isShortCircuit 是否需要高精度计算
     */
    @Test
    public void is_precise() throws Exception {
        // 一个参数是是否开启高精度默认false，第二个参数是是否开启trace。
        ExpressRunner runner = new ExpressRunner(false, false);
        DefaultContext<String, Object> context = new DefaultContext<>();
        //订单总价 = 单价 * 数量 + 首重价格 + （总重量 - 首重） * 续重单价
        context.put("单价", 1.25);
        context.put("数量", 100);
        context.put("首重价格", 125.25);
        context.put("总重量", 20.55);
        context.put("首重", 10.34);
        context.put("续重单价", 333.33);

        String express = "单价 * 数量 + 首重价格 + ( 总重量 - 首重 ) / 续重单价";
        Object totalPrice = runner.execute(express, context, null, true, false);
        System.out.println("totalPrice:" + totalPrice);
    }

    /**
     * 2、isShortCircuit 是否使用逻辑短路特性
     * @throws Exception
     */
    @Test
    public void testShortCircuit() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.setShortCircuit(true);
        runner.addOperatorWithAlias("小于", "<", "$1 小于 $2 不满足期望");
        runner.addOperatorWithAlias("大于", ">", "$1 大于 $2 不满足期望");

        IExpressContext<String, Object> expressContext = new DefaultContext<>();
        expressContext.put("违规天数", 100);
        expressContext.put("虚假交易扣分", 13);
        expressContext.put("VIP", false);
        List<String> errorInfo = new ArrayList<>();
        String expression = "2 小于 2 and (违规天数 小于 90 or 虚假交易扣分 小于 12)";
        boolean result = (Boolean)runner.execute(expression, expressContext, errorInfo, true, false);
        if (result) {
            System.out.println("result is success!");
        } else {
            System.out.println("result is fail!");
            for (String error : errorInfo) {
                System.out.println(error);
            }
        }
    }


    /**
     * 3、isTrace 是否输出所有的跟踪信息，同时还需要log级别是DEBUG级别
     */
    @Test
    public void testTrace() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.setShortCircuit(true);
        runner.addOperatorWithAlias("小于", "<", "$1 小于 $2 不满足期望");
        runner.addOperatorWithAlias("大于", ">", "$1 大于 $2 不满足期望");

        IExpressContext<String, Object> expressContext = new DefaultContext<>();
        expressContext.put("违规天数", 100);
        expressContext.put("虚假交易扣分", 13);
        expressContext.put("VIP", false);
        List<String> errorInfo = new ArrayList<>();
        String expression = "2 小于 2 and (违规天数 小于 90 or 虚假交易扣分 小于 12)";
        boolean result = (Boolean)runner.execute(expression, expressContext, errorInfo, true, true);
        if (result) {
            System.out.println("result is success!");
        } else {
            System.out.println("result is fail!");
            for (String error : errorInfo) {
                System.out.println(error);
            }
        }
    }


    /**
     * 3、自定义函数操作符获取原始的 `context` 控制上下文
     */
    @Test
    public void test() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        OperatorBase op = new OperatorContextPut("contextPut");
        runner.addFunction("contextPut", op);
        String express = "contextPut('success', 'false'); contextPut('error', '错误信息'); contextPut('warning', '提醒信息')";
        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("success", "true");
        Object result = runner.execute(express, context, null, false, true);
        System.out.println("返回结果：result：".concat(result.toString()));
        System.out.println("返回context：context：".concat(result.toString()));
    }
}
