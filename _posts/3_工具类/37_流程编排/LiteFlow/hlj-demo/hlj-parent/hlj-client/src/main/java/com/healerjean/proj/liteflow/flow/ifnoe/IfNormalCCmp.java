package com.healerjean.proj.liteflow.flow.ifnoe;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * IfNormalCCmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("IfNormalCCmp")
public class IfNormalCCmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[IfNormalCCmp#]process");
    }
}
