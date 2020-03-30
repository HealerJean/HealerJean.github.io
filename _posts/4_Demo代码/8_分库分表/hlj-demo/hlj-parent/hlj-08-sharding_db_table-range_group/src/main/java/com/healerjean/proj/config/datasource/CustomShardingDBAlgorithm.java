package com.healerjean.proj.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.*;

/**
 * @author HealerJean
 * @ClassName CustomShardingDBAlgorithm
 * @date 2020-03-29  20:48.
 * @Description
 */
@Slf4j
public class CustomShardingDBAlgorithm implements PreciseShardingAlgorithm<Long> {


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String dbName : availableTargetNames) {
            if (dbName.endsWith(shardingValue.getValue() % 2 + "")) {
                log.info("库为：{}, 主键为：{}, 最终被分到的库为：【{}】", availableTargetNames, shardingValue, dbName);
                return dbName;
            }
        }
        throw new IllegalArgumentException();
    }
}
