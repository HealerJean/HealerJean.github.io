package com.healerjean.proj.service.bizlog.service.function.impl;

import com.healerjean.proj.service.bizlog.common.enums.BizLogEnum;
import com.healerjean.proj.service.bizlog.data.po.Order;
import com.healerjean.proj.service.OrderQueryService;
import com.healerjean.proj.service.bizlog.service.function.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 *
 */
@Slf4j
@Component
public class OrderParseFunction implements IParseFunction {
    @Resource
    private OrderQueryService orderQueryService;

    @Override
    public boolean check() {
        return true;
    }

    /**
     * 函数名称
     *
     * @return functionName
     */
    @Override
    public String functionName() {
        return BizLogEnum.IParseFunctionEnum.ORDER_PARSE_FUNCTION.getFunction();
    }

    /**
     * apply
     * @param orderId orderId
     * @return String
     */
    @Override
    public String apply(String orderId) {
        log.info("[OrderParseFunction#apply] orderId:{}", orderId);
        if (StringUtils.isEmpty(orderId)) {
            return orderId;
        }
        Order order = orderQueryService.queryOrder(Long.parseLong(orderId));
        return  MessageFormatter.arrayFormat("【产品名称：[{}]-订单号：[{}]】",new Object[]{order.getProductName(), orderId}).getMessage();
    }
}
