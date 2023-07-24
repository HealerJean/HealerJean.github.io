package com.healerjean.proj.liteflow.flow.switchnode;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * NormalBCmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("NormalBCmp")
public class NormalBCmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[NormalBCmp#]process");
    }
}
