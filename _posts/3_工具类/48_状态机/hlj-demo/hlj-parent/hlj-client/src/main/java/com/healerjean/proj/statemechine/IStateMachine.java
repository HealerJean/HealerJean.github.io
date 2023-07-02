package com.healerjean.proj.statemechine;


import com.healerjean.proj.statemechine.context.StateContext;
import com.healerjean.proj.statemechine.enent.IEvent;
import com.healerjean.proj.statemechine.enums.EventExecuteResultEnum;
import com.healerjean.proj.statemechine.exception.OrderStateMarchException;
import com.healerjean.proj.statemechine.statue.IState;
import com.healerjean.proj.statemechine.statue.IStateFactory;
import com.healerjean.proj.statemechine.transition.CurrentTransition;
import com.healerjean.proj.statemechine.transition.Transitions;

/**
 * IStateMachine
 *
 * @author zhangyujin
 * @date 2023-06-28 11:06:27
 */
public interface IStateMachine<S extends IState, E extends IEvent> {
    /**
     * sendEvent
     *
     * @param event   执行时间
     * @param context 状态机上下文
     * @return 是否执行成功
     */
    default EventExecuteResultEnum sendEvent(E event, StateContext<S> context) {
        S source = this.getState(context);
        CurrentTransition<S, E> currentTransition = new CurrentTransition(source, event, this);
        S target = currentTransition.transit();
        boolean accepted = target != null && target.getState() != null;
        context.setSource(source);
        context.setTarget(target);
        EventExecuteResultEnum executeResultEnum;
        if (accepted) {
            try {
                executeResultEnum = this.handleStateChange(context, event);
            } catch (OrderStateMarchException e) {
                executeResultEnum = this.handleStateChangeError(e);
            }
        } else {
            executeResultEnum = this.handleStateChangeUnaccepted(context, event);
        }
        return executeResultEnum;
    }

    S getState(StateContext<S> context);


    <T extends Enum<T>, K extends IState<T>> IStateFactory<T, K> getStateFactory();

    Transitions getTransitions();

    EventExecuteResultEnum handleStateChange(StateContext<S> context, E event);

    EventExecuteResultEnum handleStateChangeUnaccepted(StateContext<S> context, E event);

    EventExecuteResultEnum handleStateChangeError(OrderStateMarchException exception);
}
