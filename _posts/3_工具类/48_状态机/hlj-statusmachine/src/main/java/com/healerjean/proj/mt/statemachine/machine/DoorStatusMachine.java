package com.healerjean.proj.mt.statemachine.machine;

import com.google.common.collect.ImmutableSet;
import com.healerjean.proj.mt.statemachine.dto.StatusMachineBaseData;
import com.healerjean.proj.mt.statemachine.enums.StateEnum;
import com.healerjean.proj.mt.statemachine.enums.StrategyTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:06 下午.
 * @description
 */
@Service
public class DoorStatusMachine extends AbstractStateMachine{


    private static final Set<StrategyTypeEnum> STRATE_TYPE = ImmutableSet.of(StrategyTypeEnum.USER_STRATEGY,StrategyTypeEnum.COMMAND_EXPIRE_STRATE);

    public DoorStatusMachine() {
        super(STRATE_TYPE);
    }

    @Override
    public StateEnum getSourceState(StatusMachineBaseData data) {
        return StateEnum.DoorStateEnum.OPENING;
    }



}
