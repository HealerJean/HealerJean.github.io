package com.healerjean.proj.function;

import com.healerjean.proj.beans.Order;
import com.healerjean.proj.service.IParseFunction;
import com.healerjean.proj.service.OrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 */
@Slf4j
@Component
public class OrderParseFunction implements IParseFunction {
    @Resource
    @Lazy
    private OrderQueryService orderQueryService;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "ORDER";
    }

    @Override
    public String apply(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        log.info("###########,{}", value);
        Order order = orderQueryService.queryOrder(Long.parseLong(value));
        return order.getProductName().concat("(").concat(value).concat(")");
    }
}
