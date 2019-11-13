package com.hlj.proj.config;

import com.hlj.proj.database.Database0Config;
import com.hlj.proj.database.Database1Config;
import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 这里使用的都是单键分片策略
 * 示例分库策略是：
 * GoodsId为偶数使用database0表
 * GoodsId为奇数使用database1表
 */
@Component
public class DatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {

    @Autowired
    private Database0Config database0Config;

    @Autowired
    private Database1Config database1Config;


    /**
     * sql == 规则
     * availableTargetNames(database0 database1)
     * ShardingValue(logicTableName=goods, columnName=goods_id, value=381093969037099008, values=[], valueRange=null)
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Long value = shardingValue.getValue();
        if (value.hashCode() % 2 == 0) {
            return database0Config.getDatabaseName();
        } else {
            return database1Config.getDatabaseName();
        }
    }

    /**
     * sql in 规则
     */
    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (Long value : shardingValue.getValues()) {
            if (value.hashCode() % 2 == 0) {
                result.add(database0Config.getDatabaseName());
            } else {
                result.add(database1Config.getDatabaseName());
            }
        }
        return result;
    }

    /**
     * sql between 规则
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long value = range.lowerEndpoint(); value <= range.upperEndpoint(); value++) {
            if (value.hashCode() % 2 == 0) {
                result.add(database0Config.getDatabaseName());
            } else {
                result.add(database1Config.getDatabaseName());
            }
        }
        return result;
    }
}
