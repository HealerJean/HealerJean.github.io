package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.FlowNode;
import com.hlj.proj.service.flow.base.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("demoJobSubmit")
@Slf4j
public class DemoJobSubmitFlowNode extends FlowNode {


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("DemoJob执行----demoJobSubmit-------提交执行任务");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
    }
}
