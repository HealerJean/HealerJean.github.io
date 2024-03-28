package com.healerjean.proj.utils.db;

import com.healerjean.proj.data.bo.BigKeyBO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 大Key缓存刷新工具
 *
 * @author zhangyujin
 * @date 2024/2/7
 */
public class BigKeyCacheRefreshUtils {


    /**
     * 大数据量-IdSize查询全部
     */
    public static <R> Long cacheBigCache(Function<IdQueryBO, List<R>> function, long size, Consumer<BigKeyBO> consumer) {

        BigKeyBO bigKey = new BigKeyBO();
        bigKey.setMinKeyIndex(1L);
        bigKey.setEndFlag(false);
        bigKey.setCount(0L);
        IdQueryBO idQuery = new IdQueryBO(0L, size);
        while (true) {
            List<R> dbList = function.apply(idQuery);
            if (CollectionUtils.isEmpty(dbList)) {
                bigKey.setEndFlag(true);
                consumer.accept(bigKey);
                break;
            }
            idQuery.setMinId(idQuery.getMaxId());

            List readyList = Optional.ofNullable(bigKey.getReadyList()).orElse(new ArrayList<>());
            readyList.addAll(dbList);
            bigKey.setReadyList(readyList);
            bigKey.setCount(bigKey.getCount() + dbList.size());
            consumer.accept(bigKey);
        }
        return bigKey.getCount();
    }

}
