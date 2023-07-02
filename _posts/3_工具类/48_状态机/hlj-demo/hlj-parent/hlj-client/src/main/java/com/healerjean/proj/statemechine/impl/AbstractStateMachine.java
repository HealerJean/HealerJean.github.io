package com.healerjean.proj.statemechine.impl;

import com.healerjean.proj.statemechine.IStateMachine;
import com.healerjean.proj.statemechine.context.StateContext;
import com.healerjean.proj.statemechine.enent.IEvent;
import com.healerjean.proj.statemechine.enums.EventExecuteResultEnum;
import com.healerjean.proj.statemechine.statue.IState;
import com.healerjean.proj.statemechine.statue.IStateFactory;
import com.healerjean.proj.statemechine.transition.Transitions;

/**
 * AbstractStateMachine
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:40$
 */
public abstract class AbstractStateMachine<S extends IState, E extends IEvent, Context extends StateContext<S>> implements IStateMachine<S, E> {

    /**
     * transitions
     */
    private Transitions transitions = new Transitions();


    @Override
    public Transitions getTransitions() {
        return transitions;
    }

    @Override
    public <T extends Enum<T>, K extends IState<T>> IStateFactory<T, K> getStateFactory() {
        return s -> (K) s;
    }



    /**
     * 正常流转-结果处理
     *
     * @param context context
     * @param event   event
     * @return EventExecuteResultEnum
     */
    @Override
    public EventExecuteResultEnum handleStateChange(StateContext<S> context, E event) {
        return invokeHandleStateChange((Context) context);
    }


    /**
     * 正常流转-结果处理
     *
     * @param context context
     * @return EventExecuteResultEnum
     */
    protected abstract EventExecuteResultEnum invokeHandleStateChange(Context context);


    /**
     * 失败流转-结果处理
     *
     * @param context context
     * @param event   event
     * @return EventExecuteResultEnum
     */
    @Override
    public EventExecuteResultEnum handleStateChangeUnaccepted(StateContext<S> context, E event) {
        return invokeHandleStateChangeUnaccepted((Context) context);
    }


    /**
     * 错误流转-结果处理
     *
     * @param context context
     * @return EventExecuteResultEnum
     */
    protected abstract EventExecuteResultEnum invokeHandleStateChangeUnaccepted(Context context);

}
