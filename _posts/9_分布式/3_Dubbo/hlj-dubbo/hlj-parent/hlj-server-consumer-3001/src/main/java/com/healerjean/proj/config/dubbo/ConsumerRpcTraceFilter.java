package com.healerjean.proj.config.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.dubbo.filter.FutureFilter;
import org.slf4j.MDC;


@Activate(group = Constants.CONSUMER, order = 1)
public class ConsumerRpcTraceFilter extends FutureFilter {


    private static final String DUBBO_REQ_UID = "REQ_UID";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(DUBBO_REQ_UID, MDC.get(DUBBO_REQ_UID));
        return invoker.invoke(invocation);
    }
}
