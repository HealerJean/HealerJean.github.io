package com.healerjean.proj.ddd;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderAppService
 *
 * @author zhangyujin
 * @date 2024/8/27
 */
@RestController
@RequestMapping(value = "/orders/")
public class OrderController {
    @Autowired
    private OrderAppService service;

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestParam(value = "request", required = true) CreateOrderRequest request) {
        if (request.isInvalid()) {
            throw new BadRequestException("the request of placing order is invalid.");
        }
        Order order = request.toOrder();
        service.placeOrder(order);
    }


}

@Servicepublic
class OrderAppService {
    @Autowired
    private PlaceOrderService orderService;

    public void placeOrder(Order order) {
        try {
            placeOrderService.execute(order);
        } catch (InvalidOrderException | Exception ex) {
            throw new ApplicationException(ex.getMessage());
        }
    }
}