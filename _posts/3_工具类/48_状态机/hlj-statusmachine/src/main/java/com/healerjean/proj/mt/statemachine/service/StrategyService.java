package com.healerjean.proj.mt.statemachine.service;

import com.healerjean.proj.mt.statemachine.dto.StatusMachineBaseData;
import com.healerjean.proj.mt.statemachine.enums.StrategyEnum;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:10 下午.
 * @description
 */
public interface StrategyService {

    StrategyEnum getStrategyEnum(StatusMachineBaseData data);
}
