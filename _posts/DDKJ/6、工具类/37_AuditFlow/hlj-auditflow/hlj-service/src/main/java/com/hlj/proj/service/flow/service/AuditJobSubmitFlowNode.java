package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.FlowNode;
import com.hlj.proj.service.flow.base.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("auditJobSubmit")
@Slf4j
public class AuditJobSubmitFlowNode extends FlowNode {


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("审核任务执行----auditJobSubmit-------审核任务提交");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        System.out.println("-----------审核失败-------------------");
    }

}
