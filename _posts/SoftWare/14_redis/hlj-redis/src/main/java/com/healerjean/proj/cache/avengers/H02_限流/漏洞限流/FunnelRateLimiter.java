package com.healerjean.proj.cache.avengers.H02_限流.漏洞限流;

import com.healerjean.proj.cache.avengers.H02_限流.漏洞限流.dto.FunnelDTO;

import java.util.Map;

/**
 * @author zhangyujin
 * @date 2021/5/7  3:17 下午.
 * @description
 */
public class FunnelRateLimiter {


    private Map<String, FunnelDTO> funnels ;
    public FunnelRateLimiter(Map funnels) {
        this.funnels = funnels;
    }


    /**
     * 限流方法
     * @param userId
     * @param actionKey
     * @param capacity
     * @param leakingRate
     * @return
     * 1、根据用户Id和动作获取对应的漏斗
     * 2、往漏斗中放入定额数据，看是否能放下
     */
    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
        // 1、根据用户Id和动作获取对应的漏斗
        String key = String.format("%s:%s", userId, actionKey);
        FunnelDTO funnel = funnels.get(key);
        if (funnel == null) {
            funnel = new FunnelDTO(capacity, leakingRate);
            funnels.put(key, funnel);
        }

        //2、往漏斗中放入定额数据
        return funnel.watering(1);
    }


}

