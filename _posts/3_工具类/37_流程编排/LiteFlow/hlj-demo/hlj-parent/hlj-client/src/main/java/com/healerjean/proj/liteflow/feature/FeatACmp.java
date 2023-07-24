package com.healerjean.proj.liteflow.feature;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * FeatACmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@LiteflowComponent(id = "FeatACmp", name = "FeatACmpName")
@Slf4j
public class FeatACmp extends NodeComponent {

    @Override
    public void process() {
        log.info("[FeatACmp#]process");
    }
}