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

    void startJob(DemoJobDTO demoJobDTO ,IdentityInfoDTO identityInfoDTO);
}
