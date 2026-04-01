package com.healerjean.proj.strata.client;

import com.healerjean.proj.strata.client.order.api.OrderService;
import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.client.order.param.OrderParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Test
    public void testFromDb() {
        OrderDTO orderDTO = orderService.getFromDb(new OrderParam("demo"));
        assertEquals("dongboot", orderDTO.getValue());
    }




}
