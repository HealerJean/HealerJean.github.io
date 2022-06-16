import com.healerjean.proj.Application;
import com.healerjean.proj.springmachine.entity.Order;
import com.healerjean.proj.springmachine.entity.OrderState;
import com.healerjean.proj.springmachine.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testSuccess(){
        Order order = orderService.create();

        orderService.pay(order.getId());

        orderService.deliver(order.getId());

        orderService.receive(order.getId());

        assertTrue(OrderState.FINISH == order.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testError(){
        Order order = orderService.create();
//        orderService.pay(order.getId());
        orderService.deliver(order.getId());
        orderService.receive(order.getId());
        assertTrue(OrderState.FINISH == order.getStatus());
    }

}