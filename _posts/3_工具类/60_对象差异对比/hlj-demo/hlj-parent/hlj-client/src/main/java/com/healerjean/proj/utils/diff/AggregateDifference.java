package com.healerjean.proj.utils.diff;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.ZParams;

/**
 * AggregateDifference
 *
 * @author zhangyujin
 * @date 2023/12/20
 */
@Getter
@Setter
public class AggregateDifference<T extends ZParams.Aggregate<ID>, ID extends Identifier> {

    /**
     * 快照对象
     */
    private T snapshot;

    /**
     * 追踪对象
     */
    private T aggregate;

    /**
     * 差异类型
     */
    private DifferenceType differentType;

    /**
     * 字段差异
     */
    private Map<String, FieldDifference> fieldDifferences;
}

