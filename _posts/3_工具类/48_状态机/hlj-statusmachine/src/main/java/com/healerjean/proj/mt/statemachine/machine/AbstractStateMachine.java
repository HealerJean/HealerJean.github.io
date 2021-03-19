package com.healerjean.proj.mt.statemachine.machine;

import com.healerjean.proj.mt.statemachine.StateMachine;
import com.healerjean.proj.mt.statemachine.dto.StatusMachineBaseData;
import com.healerjean.proj.mt.statemachine.enums.StateEnum;
import com.healerjean.proj.mt.statemachine.enums.StrategyEnum;
import com.healerjean.proj.mt.statemachine.enums.StrategyTypeEnum;
import com.healerjean.proj.mt.statemachine.service.StrategyService;
import com.healerjean.proj.mt.statemachine.utils.SpringContextUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:07 下午.
 * @description //写一些抽象的方法
 */
public abstract class AbstractStateMachine implements StateMachine {

    private Set<StrategyTypeEnum> strategyTypeEnums;

    public AbstractStateMachine( Set<StrategyTypeEnum> strategyTypeEnums) {
        this.strategyTypeEnums = strategyTypeEnums;
    }

    /**
     * 1、获取源状态
     * 2、获取策略
     * 3、获取目标状态
     * @param data
     * @return
     */
    @Override
    public StateEnum sendActionEvent(StatusMachineBaseData data) {
        // 1、获取源状态
        StateEnum sourceStateEnum = getSourceState(data);
        // 2、获取策略
        List<ImmutablePair<String, StrategyEnum>> strategy = getStrategy(data);
        // 3、获取目标状态

        // 4、处理业务逻辑
        return null;
    }


    /**
     * 获取策略
     * 1、如果参数中传入了策略类型则采用参数中的策略,否则执行默认的策略
     * 2、调用策略服务进行获取策略枚举
     * @param data
     * @return
     */
    @Override
    public List<ImmutablePair<String, StrategyEnum>> getStrategy(StatusMachineBaseData data) {
        List<ImmutablePair<String, StrategyEnum>> result = new ArrayList<>();
        // 1、如果参数中传入了策略类型则采用参数中的策略
        Set<StrategyTypeEnum> dealStrategyTypeEnum = null;
        if (data != null && data.getStrategyTypeEnums() != null
                && !data.getStrategyTypeEnums().isEmpty()) {
            dealStrategyTypeEnum = data.getStrategyTypeEnums();
        }

        // 2、调用策略服务进行获取策略枚举
        dealStrategyTypeEnum.stream().forEach(strategyTypeEnum -> {
            String strategyType = strategyTypeEnum.getName();
            StrategyService strategyService = SpringContextUtil.getBeanByNameAndType(strategyType, StrategyService.class);
            ImmutablePair<String, StrategyEnum> pair = ImmutablePair.of(strategyType,strategyService.getStrategyEnum(data)) ;
            result.add(pair);
        });
        return result;
    }


}
