package com.healerjean.proj.statemechine.context;


import com.healerjean.proj.statemechine.statue.IState;

/**
 * StateContext
 *
 * @author zhangyujin
 * @date 2023-06-28 10:06:06
 */
public abstract class StateContext<T extends IState> {
    private T source;
    private T target;

    public StateContext() {
    }

    /**
     * 获取当前状态机上下文
     *
     * @return 当前状态机上下文
     */
    public abstract Object fetchContext();

    public T getSource() {
        return this.source;
    }

    public void setSource(final T source) {
        this.source = source;
    }

    public T getTarget() {
        return this.target;
    }

    public void setTarget(final T target) {
        this.target = target;
    }
}
