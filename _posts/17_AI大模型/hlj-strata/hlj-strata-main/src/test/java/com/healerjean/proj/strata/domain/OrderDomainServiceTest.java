package com.healerjean.proj.strata.domain;

import com.healerjean.proj.strata.domain.order.bo.OrderBO;
import com.healerjean.proj.strata.domain.order.service.OrderDomainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderDomainServiceTest {

    @Autowired
    OrderDomainService orderDomainService;

    @Test
    public void testFromDb() {
    OrderBO orderBO = orderDomainService.getFromDb("demo");
        assertEquals("dongboot", orderBO.getValue());
    }



}
