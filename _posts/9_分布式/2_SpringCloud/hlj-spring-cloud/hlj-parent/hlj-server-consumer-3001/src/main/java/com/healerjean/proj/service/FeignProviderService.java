package com.healerjean.proj.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HealerJean
 * @ClassName FeIgnProviderService
 * @date 2020/4/9  12:40.
 * @Description
 */

@FeignClient(name = "HLJ-SERVER-PROVIDER")
public interface FeignProviderService {

    @GetMapping("api/provider/connect")
    String connectProvider();

}
