package com.healerjean.proj.service.bizlog.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * Order
 * @author zhangyujin
 * @date 2023/5/31  16:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    /**
     * orderId
     */
    private Long orderId;
    /**
     * orderNo
     */
    private String orderNo;
    /**
     * purchaseName
     */
    private String purchaseName;
    /**
     * productName
     */
    private String productName;
    /**
     * createTime
     */
    private Date createTime;

    /**
     * userId
     */
    private String userId;

}
