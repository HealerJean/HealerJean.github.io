package com.healerjean.proj.liteflow.flow.ifnoe;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeIfComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * XIFCmd
 *
 * @author zhangyujin
 * @date 2023/7/24
 */
@LiteflowComponent("XIfCmd")
@Slf4j
public class XIfCmd extends NodeIfComponent {


    @Override
    public boolean processIf() throws Exception {
        //do your biz
        return true;
    }
}