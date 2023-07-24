package com.healerjean.proj.liteflow.feature;

import com.healerjean.proj.liteflow.bo.NodePramsBO;
import com.healerjean.proj.liteflow.context.DemoContext;
import com.healerjean.proj.liteflow.context.UserContext;
import com.healerjean.proj.utils.JsonUtils;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * FeatBCmp
 *
 * @author zhangyujin
 * @date 2023 /7/24
 */
@Slf4j
@LiteflowComponent("FeatBCmp")
public class FeatBCmp extends NodeComponent {

    /**
     * 流程执行
     */
    @Override
    public void process() {
        log.info("[FeatBCmp#]process");
        // 1、高级特性，组件参数
        NodePramsBO nodePrams = this.getCmpData(NodePramsBO.class);
        log.info("[FeatBCmp#process] this.getCmpData():{}", nodePrams);

        DemoContext requestData = this.getRequestData();
        log.info("[FeatBCmp#process] requestData:{}", JsonUtils.toString(requestData));

        DemoContext demoContext = this.getContextBean(DemoContext.class);
        log.info("[FeatBCmp#process] demoContext:{}", JsonUtils.toString(demoContext));

        UserContext userContext = this.getContextBean(UserContext.class);
        log.info("[FeatBCmp#process] userContext:{}", JsonUtils.toString(userContext));
    }

}