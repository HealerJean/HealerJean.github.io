package com.healerjean.proj.statemechine.transition;

import com.healerjean.proj.statemechine.IStateMachine;
import com.healerjean.proj.statemechine.enent.IEvent;
import com.healerjean.proj.statemechine.statue.IState;
import com.healerjean.proj.statemechine.statue.IStateFactory;

/**
 * Transition
 *
 * @author zhangyujin
 * @date 2023-06-28 11:06:25
 */
public class CurrentTransition<S extends IState, E extends IEvent> {
    private final S source;
    private final E event;
    private final IStateMachine<S, E> stateMachine;

    public CurrentTransition(final S source, final E event, final IStateMachine<S, E> stateMachine) {
        this.source = source;
        this.event = event;
        this.stateMachine = stateMachine;
    }

    public S transit() {
        IStateFactory stateFactory = this.stateMachine.getStateFactory();
        return (S) stateFactory.buildState(this.stateMachine.getTransitions().accept(this.source.getState(), this.event.getEvent()));
    }


    public S getSource() {
        return this.source;
    }

    public E getEvent() {
        return this.event;
    }
}
