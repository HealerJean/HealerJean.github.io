package com.hlj.proj.api.point;

import com.hlj.proj.dto.Point.EventDTO;

/**
 * @author HealerJean
 * @ClassName PointEventService
 * @date 2019/9/9  15:21.
 * @Description
 */
public interface PointEventService {

    /**
     * 添加事件
     * @param eventDTO
     */
    void addEvent(EventDTO eventDTO);

    /**
     * 执行事件
     */
    void executeAddPointEvent(EventDTO eventDTO);


}
