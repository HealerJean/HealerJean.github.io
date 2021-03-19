package com.healerjean.proj.mt.statemachine.service.impl;

import com.healerjean.proj.mt.statemachine.dto.StatusMachineBaseData;
import com.healerjean.proj.mt.statemachine.enums.StrategyEnum;
import com.healerjean.proj.mt.statemachine.service.StrategyService;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:12 下午.
 * @description
 */
@Service
public class CommandExpireStrategyService implements StrategyService {

    @Override
    public StrategyEnum getStrategyEnum(StatusMachineBaseData data) {
        return null;
    }
}
