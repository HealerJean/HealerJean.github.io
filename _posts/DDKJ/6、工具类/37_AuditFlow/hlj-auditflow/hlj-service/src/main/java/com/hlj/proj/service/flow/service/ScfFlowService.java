package com.hlj.proj.service.flow.service;


import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.AuditorResult;

/**
 * @ClassName ScfFlowService
 * @Author TD
 * @Date 2019/6/19 15:34
 * @Description 供应链流程审批业务类
 */
public interface ScfFlowService {


    /**
     *  对指定审批节点进行审批
     */
    void audit(AuditorResult result, IdentityInfoDTO identityInfo);

}
