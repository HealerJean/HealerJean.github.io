package com.hlj.util.z029_定时任务分片;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/4/14  3:29 下午.
 * @description
 */
public class ShardItemContextUtils {

    /**
     * 1、如果 ids 为空，则返回空集合
     * 2、分片值为空，说明该机器没有获取到分片，则返回空集合
     * 3、hash取模，确定result
     *
     * @param ids
     * @return
     */
    public static <T> List<T> getIds(List<T> ids) {
        // 1、如果 userIds 为空，则返回空集合
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        // 2、分片值为空，说明该机器没有获取到分片，则返回空集合
        // 获取分片值
        // List<Integer> shardItems = ShardItemsContext.getShardItems();
        //获取分片总数，管理端配置的分片数
        // int shardCount = ShardItemsContext.getShardCount();

        List<Integer> shardItems = Arrays.asList(1, 2, 5, 6);
        int shardCount = 8;
        if (CollectionUtils.isEmpty(shardItems)) {
            return Collections.EMPTY_LIST;
        }

        // 3、hash取模，确定result
        List<T> result = new ArrayList<>();
        ids.stream().forEach(id -> {
            if (shardItems.contains(Math.abs(id.hashCode()) % shardCount)) {
                result.add(id);
            }
        });
        return result;
    }


    @Test
    public void test(){
        List<Object> list = Arrays.asList(1,2,3);
        System.out.println(getIds(list));
    }
}
