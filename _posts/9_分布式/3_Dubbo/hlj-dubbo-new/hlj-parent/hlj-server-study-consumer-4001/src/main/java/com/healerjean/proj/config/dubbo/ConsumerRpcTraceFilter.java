package com.healerjean.proj.config.dubbo;


import com.healerjean.proj.constants.DubboConstans;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.dubbo.filter.FutureFilter;
import org.slf4j.MDC;

@Activate(group = DubboConstans.DUBBO_CONSUMER, order = 1)
public class ConsumerRpcTraceFilter extends FutureFilter {

    private static final String DUBBO_REQ_UID = "REQ_UID";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(DUBBO_REQ_UID, MDC.get(DUBBO_REQ_UID));
        return invoker.invoke(invocation);
    }


}
