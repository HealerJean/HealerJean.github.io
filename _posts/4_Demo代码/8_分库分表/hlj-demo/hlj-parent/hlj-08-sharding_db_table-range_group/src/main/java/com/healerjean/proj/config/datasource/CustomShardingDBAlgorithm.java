package com.healerjean.proj.config.datasource;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.*;

/**
 * @author HealerJean
 * @ClassName CustomShardingDBAlgorithm
 * @date 2020-03-29  20:48.
 * @Description
 */
public class CustomShardingDBAlgorithm implements PreciseShardingAlgorithm<Long> {

    private static List<ShardingRangeConfig> configs = new ArrayList<>();

    static {
        ShardingRangeConfig config = new ShardingRangeConfig();
        config.setStart(1);
        config.setEnd(30);
        config.setDatasourceList(Arrays.asList("ds0", "ds1"));
        configs.add(config);

    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Optional<ShardingRangeConfig> configOptional = configs.stream().filter(
                c -> shardingValue.getValue() >= c.getStart() && shardingValue.getValue() <= c.getEnd()).findFirst();
        if (configOptional.isPresent()) {
            ShardingRangeConfig rangeConfig = configOptional.get();
            for (String ds : rangeConfig.getDatasourceList()) {
                if (ds.endsWith(shardingValue.getValue() % 2 + "")) {
                    System.err.println(ds);
                    return ds;
                }
            }
        }
        throw new IllegalArgumentException();
    }

}
