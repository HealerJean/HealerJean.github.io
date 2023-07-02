package com.healerjean.proj.statemechine.statue;

/**
 * StateFactory
 *
 * @author zhangyujin
 * @date 2023-06-28 11:06:09
 */

public interface IStateFactory<S extends Enum<S>, T extends IState<S>> {

    /**
     * 获取状态
     *
     * @param enumState enumState
     * @return T
     */
    T buildState(S enumState);
}
