package com.healerjean.proj.statemechine.statue;


/**
 * State 状态
 *
 * @author zhangyujin
 * @date 2023-06-28 09:06:33
 */

public interface IState<S extends Enum<S>> {

    S getState();
}
