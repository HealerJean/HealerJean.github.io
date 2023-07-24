package com.healerjean.proj.liteflow.feature;

import com.healerjean.proj.liteflow.bo.NodePramsBO;
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
    }


}