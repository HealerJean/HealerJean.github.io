package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.IOrderService;
import com.healerjean.proj.service.UserQueryService;
import com.healerjean.proj.service.bizlog.anno.LogRecordAnnotation;
import com.healerjean.proj.service.bizlog.common.BizLogConstants;
import com.healerjean.proj.service.bizlog.data.po.Order;
import com.healerjean.proj.service.bizlog.utils.LogTheadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OrderServiceImpl
 * @author zhangyujin
 * @date 2023/5/31  16:10
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    /**
     * userQueryService
     */
    @Resource
    private UserQueryService userQueryService;

    /*'张三下了一个订单,购买商品「超值优惠红烧肉套餐」,下单结果:true' */
    @Override
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            category = "MANAGER_VIEW",
            detail = "{{#order.toString()}}",
            operator = "{{#currentUser}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            prefix = BizLogConstants.BizLogTypeConstant.ORDER_TYPE,
            bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo: {}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogTheadLocal.putVariable("innerOrder", order1);
        LogTheadLocal.putVariable("currentUser", "healerJean");
        return true;
    }

    @Override
    @LogRecordAnnotation(success = "更新了订单{orderParse{#order.orderId}},更新内容为...",
            prefix = BizLogConstants.BizLogTypeConstant.ORDER_TYPE,
            bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}")
    public boolean update(Long orderId, Order order) {
        order.setOrderId(10000L);
        return false;
    }

    @Override
    @LogRecordAnnotation(success = "更新了订单{orderParse{#orderId}},更新内容为...",
            prefix = BizLogConstants.BizLogTypeConstant.ORDER_TYPE,
            bizNo = "{{#order.orderNo}}",
            condition = "{{#condition == null}}")
    public boolean testCondition(Long orderId, Order order, String condition) {
        return false;
    }

    @Override
    @LogRecordAnnotation(success = "更新了订单{orderParse{#orderId}},更新内容为..{{#title}}}",
            prefix = BizLogConstants.BizLogTypeConstant.ORDER_TYPE,
            bizNo = "{{#order.orderNo}}")
    public boolean testContextCallContext(Long orderId, Order order) {
        LogTheadLocal.putVariable("title", "外层调用");
        userQueryService.getUser(order.getUserId());
        return false;
    }
}
