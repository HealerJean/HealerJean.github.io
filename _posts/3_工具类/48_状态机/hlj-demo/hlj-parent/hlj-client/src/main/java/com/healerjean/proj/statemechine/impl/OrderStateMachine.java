package com.healerjean.proj.statemechine.impl;

import com.healerjean.proj.statemechine.context.OrderStateEventContext;
import com.healerjean.proj.statemechine.context.StateContext;
import com.healerjean.proj.statemechine.enent.OrderEventEnum;
import com.healerjean.proj.statemechine.enums.EventExecuteResultEnum;
import com.healerjean.proj.statemechine.exception.OrderStateMarchException;
import com.healerjean.proj.statemechine.statue.OrderStatusEnum;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * OrderStateMarchine
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:40$
 */
@Slf4j
@Component
public class OrderStateMachine extends AbstractStateMachine<OrderStatusEnum, OrderEventEnum, OrderStateEventContext> {


    /**
     * 执行状态机
     *
     * @param context context
     * @return EventExecuteResultEnum
     */
    public EventExecuteResultEnum invokeSendEvent(OrderStateEventContext context) {
        OrderEventEnum orderEventEnum = context.getOrderEventEnum();
        EventExecuteResultEnum executeResultEnum;
        try {
            executeResultEnum = sendEvent(orderEventEnum, context);
        } catch (Exception e) {
            log.error("[OrderStateMachine#invokeSendEvent] error, context:{}", JsonUtils.toString(context), e);
            return EventExecuteResultEnum.FAIL_EXECUTE_ERROR;
        }
        log.info("[OrderStateMachine#invokeSendEvent] context:{}, executeResultEnum:{}", JsonUtils.toString(context), executeResultEnum);
        return executeResultEnum;
    }

    /**
     * getState
     *
     * @param context context
     * @return OrderStatusEnum
     */
    @Override
    public OrderStatusEnum getState(StateContext<OrderStatusEnum> context) {
        // todo 获取源状态
        return OrderStatusEnum.INIT;
    }

    /**
     * 成功-结果处理
     *
     * @param context context
     * @return EventExecuteResultEnum
     */
    @Override
    protected EventExecuteResultEnum invokeHandleStateChange(OrderStateEventContext context) {
        // 模拟异常
        //     throw new OrderStateMarchException("9999", "测试异常", context);
        // todo 业务处理
        return EventExecuteResultEnum.SUCCESS;
    }


    /**
     * 失败-结果处理
     *
     * @param context context
     * @return EventExecuteResultEnum
     */
    @Override
    protected EventExecuteResultEnum invokeHandleStateChangeUnaccepted(OrderStateEventContext context) {
        // todo 业务处理
        return EventExecuteResultEnum.FAIL_CONFIG_EMPTY;
    }

    /**
     * 异常-结果处理
     *
     * @param exception context
     * @return EventExecuteResultEnum
     */
    @Override
    public EventExecuteResultEnum handleStateChangeError(OrderStateMarchException exception) {
        // todo 业务处理
        return EventExecuteResultEnum.FAIL_EXECUTE_ERROR;
    }


}
