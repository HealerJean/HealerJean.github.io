package com.healerjean.proj.service;

import com.healerjean.proj.data.bo.OrderBO;

/**
 * @author zhangyujin
 * @date 2023/6/28$  12:49$
 */
public interface OrderService {


    /**
     * 状态刘庄
     * @param order
     * @return
     */
    boolean sendEvent(OrderBO order);

}
