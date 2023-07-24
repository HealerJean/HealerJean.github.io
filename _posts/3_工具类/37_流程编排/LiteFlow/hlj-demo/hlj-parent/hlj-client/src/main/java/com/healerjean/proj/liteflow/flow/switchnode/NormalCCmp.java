package com.healerjean.proj.liteflow.flow.switchnode;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * SwitchCCmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("NormalCCmp")
public class NormalCCmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[NormalCCmp#]process");
    }
}
