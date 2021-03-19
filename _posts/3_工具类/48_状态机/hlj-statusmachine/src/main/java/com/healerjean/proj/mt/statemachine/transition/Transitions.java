package com.healerjean.proj.mt.statemachine.transition;


import com.healerjean.proj.mt.statemachine.enums.StrategyTypeEnum;

import java.util.Map;
import java.util.Set;

/**
 * @author lujunyu
 * @date 2020/8/3
 */
public class Transitions<State extends  Enum<State>> {


    private Map<String, Set<StrategyTypeEnum>> strategyMap;



}
