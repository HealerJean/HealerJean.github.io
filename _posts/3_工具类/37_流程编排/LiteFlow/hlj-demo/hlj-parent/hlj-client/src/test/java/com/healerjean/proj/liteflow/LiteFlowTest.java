package com.healerjean.proj.liteflow;

import com.healerjean.proj.base.BaseJunit5SpringTest;
import com.healerjean.proj.liteflow.context.DemoContext;
import com.healerjean.proj.liteflow.context.UserContext;
import com.healerjean.proj.utils.JsonUtils;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * LiteFlowTest
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@DisplayName("LiteFlowTest")
@Slf4j
public class LiteFlowTest extends BaseJunit5SpringTest {

    @Resource
    private FlowExecutor flowExecutor;

    @DisplayName("普通组件")
    @Test
    public void test(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("普通组件");
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", demoContext);
        log.info("[LiteFlowTest#普通组件] res:{}", JsonUtils.toString(response));

        DemoContext contextBean = response.getSlot().getRequestData();
        log.info("[LiteFlowTest#条件组件] contextBean:{}", JsonUtils.toString(contextBean));

        String executeStepStrWithTime = response.getExecuteStepStrWithTime();
        log.info("[LiteFlowTest#条件组件] executeStepStrWithTime:{}", JsonUtils.toString(executeStepStrWithTime));

    }



    @DisplayName("选择组件-根据NodeId进行选择")
    @Test
    public void test2(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("选择组件");
        LiteflowResponse response = flowExecutor.execute2Resp("switchChain1", demoContext);
        log.info("[LiteFlowTest#选择组件] res:{}", JsonUtils.toString(response));
    }


    @DisplayName("选择组件-根据表达式的id进行选择")
    @Test
    public void test3(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("选择组件");
        LiteflowResponse response = flowExecutor.execute2Resp("switchChain2", demoContext);
        log.info("[LiteFlowTest#选择组件] res:{}", JsonUtils.toString(response));
    }


    @DisplayName("选择组件-根据tag进行选择")
    @Test
    public void test4(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("选择组件");
        LiteflowResponse response = flowExecutor.execute2Resp("switchChain3", demoContext);
        log.info("[LiteFlowTest#选择组件] res:{}", JsonUtils.toString(response));
    }


    @DisplayName("选择组件-表达式tag的选择")
    @Test
    public void test5(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("选择组件");
        LiteflowResponse response = flowExecutor.execute2Resp("switchChain4", demoContext);
        log.info("[LiteFlowTest#选择组件] res:{}", JsonUtils.toString(response));
    }

    @DisplayName("选择组件-链路tag的选择")
    @Test
    public void test6(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("选择组件");
        LiteflowResponse response = flowExecutor.execute2Resp("switchChain5", demoContext);
        log.info("[LiteFlowTest#选择组件] res:{}", JsonUtils.toString(response));
    }


    @DisplayName("条件组件")
    @Test
    public void test7(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("条件组件");
        LiteflowResponse response = flowExecutor.execute2Resp("ifChain1", demoContext);
        log.info("[LiteFlowTest#条件组件] res:{}", JsonUtils.toString(response));
    }


    /**
     * Test feat 001.
     */
    @DisplayName("高级特性-组件化参数")
    @Test
    public void testFeat001(){
        DemoContext demoContext = new DemoContext();
        demoContext.setBusinessType("高级特性");

        LiteflowResponse response = flowExecutor.execute2Resp("featChain1", demoContext, DemoContext.class, UserContext.class);
        log.info("[LiteFlowTest#高级特性] res:{}", JsonUtils.toString(response));



        LiteflowResponse response2 = flowExecutor.execute2Resp("featChain1", demoContext, DemoContext.class);
        log.info("[LiteFlowTest#高级特性] res:{}", JsonUtils.toString(response2));
    }





}
