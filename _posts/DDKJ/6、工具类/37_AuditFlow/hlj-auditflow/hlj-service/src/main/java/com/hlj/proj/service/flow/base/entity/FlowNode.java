package com.hlj.proj.service.flow.base.entity;


import com.hlj.proj.dto.user.IdentityInfoDTO;
import lombok.Data;


@Data
public abstract class FlowNode {

    /**
     * 节点编号
     */
    private String nodeCode;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;


    /**
     * 执行该节点业务
     */
    protected abstract Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo);

    /**
     * 节点执行失败，需要处理的业务流程
     */
    protected abstract void fail(String instantsNo, String data, IdentityInfoDTO identityInfo);


}
