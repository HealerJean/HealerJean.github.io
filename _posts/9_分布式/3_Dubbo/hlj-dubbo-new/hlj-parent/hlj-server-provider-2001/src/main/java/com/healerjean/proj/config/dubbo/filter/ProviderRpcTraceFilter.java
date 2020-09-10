package com.healerjean.proj.config.dubbo.filter;

import com.healerjean.proj.constants.DubboConstans;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.dubbo.filter.FutureFilter;
import org.slf4j.MDC;

import java.util.UUID;

@Activate(group = DubboConstans.DUBBO_PROVOIDER, order = 1)
public class ProviderRpcTraceFilter extends FutureFilter {

    private static final String DUBBO_REQ_UID = "REQ_UID";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String reqUid = RpcContext.getContext().getAttachment(DUBBO_REQ_UID);
        if (StringUtils.isBlank(reqUid)) {
            //传递丢失
            reqUid = "_:" + UUID.randomUUID().toString().replace("-", "");
            RpcContext.getContext().setAttachment(DUBBO_REQ_UID, reqUid);
        }
        MDC.put(DUBBO_REQ_UID, reqUid);
        try {
            return invoker.invoke(invocation);
        } finally {
            MDC.remove(DUBBO_REQ_UID);
        }
    }
}
