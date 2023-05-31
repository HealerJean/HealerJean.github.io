package com.hlj.proj.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * BusinessDTO
 * @author zhangyujin
 * @date 2023/5/30  13:49.
 */
@Accessors(chain = true)
@Data
public class BusinessDTO {

    /**
     * 操作人
     */
    private String operator;

    /**
     * 动作
     */
    private String action;

    /**
     * 订单号
     */
    private String orderId;
}
