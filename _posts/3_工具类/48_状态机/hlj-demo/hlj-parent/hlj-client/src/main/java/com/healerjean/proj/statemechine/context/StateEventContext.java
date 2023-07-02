package com.healerjean.proj.statemechine.context;

import com.healerjean.proj.statemechine.enent.IEvent;
import com.healerjean.proj.statemechine.statue.IState;

/**
 * StateEventContext
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:32$
 */
public abstract class StateEventContext<S extends IState, E extends IEvent> extends StateContext<S> {

    /**
     * fetchEvent
     */
    abstract protected E fetchEvent();
}
