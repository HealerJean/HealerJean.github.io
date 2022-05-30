package com.healerjean.proj.client;

import com.healerjean.proj.server.JmeterServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;

/**
 * jmeter测试脚本
 *
 * @author zhangyujin
 * @date 2022/5/30  11:05.
 */
@Slf4j
public class JavaClient extends AbstractJavaSamplerClient {


    /**
     * 把测试的一些默认数据在程序运行前显示到JMeter客户端
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("num1", "num1");
        params.addArgument("num2", "num2");
        return params;
    }

    /**
     * 子类用它来 记录log
     *
     * @return
     */
    @Override
    protected org.apache.log.Logger getLogger() {
        return null;
    }


    /**
     * 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行
     *
     * @param context
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        log.info("[JavaClient#setupTest] num1:{}, num2:{}", num1, num2);
    }

    /**
     * 结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        log.info("[JavaClient#teardownTest] num1:{}, num2:{}", num1, num2);
    }

    /**
     * @Test只是为了调试用，最后打jar包的时候注释掉。
     */
    @Test
    public void test() {

    }

    public static void main(String[] args) {
        //设置参数，并赋予默认值2
        Arguments params = new Arguments();
        params.addArgument("num1", "1");
        params.addArgument("num2", "2");
        JavaSamplerContext arg0 = new JavaSamplerContext(params);
        JavaClient test = new JavaClient();
        test.setupTest(arg0);

        test.runTest(arg0);

        test.teardownTest(arg0);
    }


    /**
     * 测试执行的循环体，根据线程数和循环次数的不同可执行多次
     * 1、获取界面中传递的值
     * 2、压测结果获取
     * 3、判断测试成功与否的方法：可根据实际进行判断，此处为如果结果非空，则认为该次调用成功
     *
     * @param context
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleresult = new SampleResult();
        // 1、获取界面中传递的值
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        sampleresult.sampleStart();//计时开始
        try {
            // 2、压测结果获取
            JmeterServer test = new JmeterServer();
            String result = test.yc();
            //将结果写入结果树：在jmeter的监听器-查看结果树时即可查看返回结果
            sampleresult.setResponseData("结果是：" + result + "," + num1 + "," + num2, null);
            sampleresult.setDataType(SampleResult.TEXT);
            sampleresult.setSuccessful(true);

        } catch (Exception e) {
            //不满足判断条件则判为false，会出现在监听器-聚合报告的Error%列
            sampleresult.setSuccessful(false);
            e.printStackTrace();
        } finally {
            //计时结束
            sampleresult.sampleEnd();
        }

        // 3、判断测试成功与否的方法：可根据实际进行判断，此处为如果结果非空，则认为该次调用成功
        // if (result.equals("a")) {
        //     //将结果写入结果树：在jmeter的监听器-查看结果树时即可查看返回结果
        //     sampleresult.setSuccessful( true);
        //     sampleresult.setResponseData( "结果是："+a ,null);
        //     sampleresult.setDataType(SampleResult. TEXT);
        // } else {
        //     sampleresult.setSuccessful( false);//不满足判断条件则判为false，会出现在监听器-聚合报告的Error%列
        // }
        return sampleresult;
    }

}
