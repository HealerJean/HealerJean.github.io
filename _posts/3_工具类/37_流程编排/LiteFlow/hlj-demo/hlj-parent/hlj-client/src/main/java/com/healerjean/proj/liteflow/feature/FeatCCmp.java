package com.healerjean.proj.liteflow.feature;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * FeatCCmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("FeatCCmp")
public class FeatCCmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[FeatCCmp#]process");
    }
}