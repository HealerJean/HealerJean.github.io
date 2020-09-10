package com.healerjean.proj.service;

import com.healerjean.proj.dto.UserDTO;

/**
 * @author HealerJean
 * @ClassName FeIgnProviderService
 * @date 2020/4/9  12:40.
 * @Description dubbo接口
 */
public interface ProviderDubboService {

    UserDTO connect(String name);

}
