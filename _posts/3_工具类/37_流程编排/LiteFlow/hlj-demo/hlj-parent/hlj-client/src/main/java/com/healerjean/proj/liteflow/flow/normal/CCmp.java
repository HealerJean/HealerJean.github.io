package com.healerjean.proj.liteflow.flow.normal;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * CCmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("流程C")
public class CCmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[CCmp#]process");
    }
}