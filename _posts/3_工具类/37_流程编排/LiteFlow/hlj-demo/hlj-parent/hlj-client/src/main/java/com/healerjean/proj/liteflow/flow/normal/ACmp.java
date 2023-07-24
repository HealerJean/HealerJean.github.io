package com.healerjean.proj.liteflow.flow.normal;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * ACmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@LiteflowComponent("流程A")
@Slf4j
public class ACmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[ACmp#]process");
    }
}