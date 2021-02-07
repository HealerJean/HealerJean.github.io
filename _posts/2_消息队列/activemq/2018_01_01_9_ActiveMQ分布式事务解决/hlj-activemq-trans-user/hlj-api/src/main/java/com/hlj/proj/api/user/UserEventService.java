package com.hlj.proj.api.user;

import com.hlj.proj.dto.user.EventDTO;

/**
 * @author HealerJean
 * @ClassName UserEventService
 * @date 2019/9/9  14:28.
 * @Description
 */
public interface UserEventService {

    /**
     * 添加事件
     * @param eventDTO
     */
    void addEvent(EventDTO eventDTO);

    /**
     * 执行事件
     */
    void executeAddUserEvent(EventDTO eventDTO);
}
