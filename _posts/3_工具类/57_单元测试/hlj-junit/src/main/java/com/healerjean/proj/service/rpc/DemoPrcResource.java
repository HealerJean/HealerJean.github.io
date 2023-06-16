package com.healerjean.proj.service.rpc;

import org.springframework.stereotype.Service;

/**
 * DemoPrcResource
 *
 * @author zhangyujin
 * @date 2023/6/15  21:29.
 */
@Service("demoPrcResource")
public class DemoPrcResource {

    /**
     * rpcInvoke
     *
     * @return String
     */
    public String rpcInvoke(String reqStr) {
        return reqStr + "远程接口";
    }
}
