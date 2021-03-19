package com.healerjean.proj.mt.statemachine;

import com.healerjean.proj.mt.statemachine.dto.StatusMachineBaseData;
import com.healerjean.proj.mt.statemachine.enums.ActionEventEnum;
import com.healerjean.proj.mt.statemachine.enums.StateEnum;
import com.healerjean.proj.mt.statemachine.enums.StrategyEnum;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/3/5  4:39 下午.
 * @description
 */
public interface StateMachine{

     StateEnum getSourceState(StatusMachineBaseData data);


     List<ImmutablePair<String, StrategyEnum>> getStrategy(StatusMachineBaseData data);


     StateEnum sendActionEvent(StatusMachineBaseData data);
}
