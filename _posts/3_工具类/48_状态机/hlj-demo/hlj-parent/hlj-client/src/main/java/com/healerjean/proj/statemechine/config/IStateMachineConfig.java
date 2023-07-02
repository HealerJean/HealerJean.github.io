package com.healerjean.proj.statemechine.config;

import com.healerjean.proj.statemechine.transition.Transitions;

/**
 * @author zhangyujin
 * @date 2023/6/28$  09:36$
 */
public interface IStateMachineConfig<State extends Enum<State>, Event extends Enum<Event>> {


    /**
     * configure
     *
     * @param transitions transitions
     */
    void configure(Transitions<State, Event> transitions) throws Exception;
}
