package com.healerjean.proj.cache.avengers.H02_限流.漏洞限流.dto;

import lombok.Data;

/**
 * @author zhangyujin
 * @date 2021/5/7  3:20 下午.
 * @description
 */
@Data
public class FunnelDTO {
    /** 容量 */
    Integer capacity;
    /** 剩余容量 */
    Integer leftQuota;
    /** 流出速率 */
    Float leakingRate;
    /** 计算起始时间 */
    Long startTime;


    public FunnelDTO(int capacity, float leakingRate) {
        this.capacity = capacity;
        this.leftQuota = capacity;
        this.leakingRate = leakingRate;
        this.startTime = System.currentTimeMillis();
    }


    /**
     * 1、计算时间间隔
     * 2、计算时间间隔内流出的数量
     *  2.1、如果获取到的小于0， 说明间隔时间太长（没有往漏斗里放数据，漏斗空了），整数数字过大溢出了，则初始化数据
     * 2.2、如果获取到的流出量小于1，则说明腾出空间太小(最小单位是1)，则直接返回，不执行
     * 3、
     */
    public void makeSpace() {
        Long nowTime = System.currentTimeMillis();
        // 1、计算时间间隔
        Long deltaTs = nowTime - startTime;

        // 2、计算时间间隔内流出的数量
        Integer deltaQuota = (int) (deltaTs * leakingRate);
        // 2.1、如果获取到的小于0， 说明间隔时间太长（没有往漏斗里放数据，漏斗空了），整数数字过大溢出了，则初始化数据
        if (deltaQuota < 0) {
            this.leftQuota = capacity;
            this.startTime = nowTime;
            return;
        }
        // 2.2、如果获取到的流出量小于1，则说明腾出空间太小(最小单位是1)，则直接返回，不执行
        if (deltaQuota < 1) {
            return;
        }

        // 3、重新计算
        // 当前时间 = nowTime
        // 剩余容量 = 当前剩余容量 + 流出速率 * 间隔时间（如果超出了总容量，剩余容量 = 总容量）
        this.leftQuota += deltaQuota;
        this.startTime = nowTime;
        if (this.leftQuota > this.capacity) {
            this.leftQuota = this.capacity;
        }
    }

    /**
     * 判断是否能加入交易
     * 1、漏斗重新计算剩余容量和当前时间
     * 2、
     * @param quota 定额
     * @return
     */
    public boolean watering(int quota) {
        // 1、漏斗重新计算剩余容量和当前时间
        makeSpace();

        // 2、如果剩余容量大于当前要流入漏斗的量，则执行成功，返回true
        if (this.leftQuota >= quota) {
            this.leftQuota -= quota;
            return true;
        }
        return false;
    }

}