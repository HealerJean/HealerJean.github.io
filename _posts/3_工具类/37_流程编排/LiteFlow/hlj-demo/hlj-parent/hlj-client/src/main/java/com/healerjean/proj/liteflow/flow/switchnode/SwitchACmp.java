package com.healerjean.proj.liteflow.flow.switchnode;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * SwitchACmp
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@Slf4j
@LiteflowComponent("SwitchACmp")
public class SwitchACmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        log.info("SwitchACmp executed!");
        // 1、根据nodeId进行选择
        // return "NormalCCmp";

        // 2、根据表达式的id进行选择
        // return "1Id";

        // 3、根据tag进行选择
        // return "tag:TagB";

        // 4、表达式tag的选择
        // return "tag:TagC";

        // 5、链路tag的选择
        return "tag:TagC";
    }
}
