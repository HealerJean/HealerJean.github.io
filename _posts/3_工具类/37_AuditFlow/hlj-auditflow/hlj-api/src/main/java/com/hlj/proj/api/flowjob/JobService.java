package com.hlj.proj.api.flowjob;

import com.hlj.proj.dto.flowJob.DemoJobDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;

/**
 * @author HealerJean
 * @ClassName JobService
 * @date 2019/8/10  14:27.
 * @Description
 */
public interface JobService {


    /**
     * 启动任务
     */
    void startJob(DemoJobDTO demoJobDTO, IdentityInfoDTO identityInfoDTO);


    /**
     * 继续执行任务
     */
    void continueJob(DemoJobDTO demoJobDTO, IdentityInfoDTO identityInfoDTO);










    /**
     * 审核任务的测试启动
     */
    void auditFlowStartJob(DemoJobDTO demoJobDTO, IdentityInfoDTO identityInfoDTO);


    /**
     * 审核任务的继续
     */
    void continueAuditFlowJob(DemoJobDTO demoJobDTO, IdentityInfoDTO identityInfoDTO);

}
