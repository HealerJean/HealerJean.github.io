package com.healerjean.proj.mt.statemachine.dto;

import com.healerjean.proj.mt.statemachine.enums.StateEnum;
import com.healerjean.proj.mt.statemachine.enums.StrategyTypeEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:03 下午.
 * @description
 */
@Data
public class StatusMachineBaseData {


    private StateEnum sourceStateEnum;
    private StateEnum targetStateEnum;
    private Set<StrategyTypeEnum> strategyTypeEnums;
}
