package com.healerjean.proj.statemechine.enent;

/**
 * Event 动作
 *
 * @author zhangyujin
 * @date 2023-06-28 09:06:45
 */
public interface IEvent<E extends Enum<E>> {

    /**
     * getEvent
     * @return E
     */
    E getEvent();
}
