package com.healerjean.proj.strata.infra;

import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;
import com.healerjean.proj.strata.infra.order.gateway.OrderGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderGatewayTest {

    @Autowired
    private OrderGateway orderGateway;

    @Test
    public void testGetByIdFromDb() {
        OrderDO orderDO = orderGateway.getByIdFromDb("demo");
        assertEquals("dongboot", orderDO.getValue());
    }


}
