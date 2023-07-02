package com.healerjean.proj.data.bo;

import com.healerjean.proj.statemechine.enent.OrderEventEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OrderBO
 *
 * @author zhangyujin
 * @date 2023/6/28$  12:50$
 */
@Accessors(chain = true)
@Data
public class OrderBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4256859893518411851L;
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * eventEnum
     */
    private OrderEventEnum eventEnum;


}
