package com.healerjean.proj.utils.db;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.healerjean.proj.common.contants.RedisConstants;
import com.healerjean.proj.data.bo.BigKeyDataBO;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.JsonUtils;
import com.healerjean.proj.utils.SpringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 大Key缓存刷新工具
 *
 * @author zhangyujin
 * @date 2024/2/7
 */
public class BigKeyCacheUtils {


    /**
     * 大数据量-IdSize查询全部
     */
    public static <T> BigKeyDataBO<T> cacheDataByCursorId(Function<IdQueryBO, List<T>> function, long dbSize, RedisConstants.BigCacheEnum bigCacheEnum) {
        BigKeyDataBO<T> bigKeyData = new BigKeyDataBO<>();
        bigKeyData.setIndex(1L);
        bigKeyData.setEndFlag(false);
        bigKeyData.setCacheEnum(bigCacheEnum);
        bigKeyData.setReadyCacheList(Lists.newArrayList());
        bigKeyData.setKeyName(bigCacheEnum.join());
        bigKeyData.setUuid(String.valueOf(System.currentTimeMillis()));
        IdQueryBO idQuery = new IdQueryBO(0L, dbSize);
        while (true) {
            // 1、分页查询
            List<T> dbList = function.apply(idQuery);
            // 2、查询结果为空将剩余待缓存的结果缓存
            if (CollectionUtils.isEmpty(dbList)) {
                bigKeyData.setEndFlag(true);
                cacheBigDayData(bigKeyData);
                break;
            }

            // 3、收集待缓存的数据准备缓存
            idQuery.setMinId(idQuery.getMaxId());
            bigKeyData.addReadyCacheRecords(dbList);
            cacheBigDayData(bigKeyData);
        }
        return bigKeyData;
    }


    /**
     * 大数据量-IdSize查询全部
     */
    public static <R> void cacheBigDayData(BigKeyDataBO<R> bigKeyBo) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        RedisConstants.BigCacheEnum cacheEnum = bigKeyBo.getCacheEnum();
        String uuid = bigKeyBo.getUuid();
        int size = cacheEnum.getValueSize();
        List<R> readyCacheList = bigKeyBo.getReadyCacheList();

        // 1、分批缓存数据
        while (readyCacheList.size() >= size) {
            List<List<R>> partition = Lists.partition(readyCacheList, size);
            List<R> removes = Lists.newArrayList();
            for (List<R> r : partition) {
                if (r.size() < size) {
                    break;
                }
                bigKeyBo.setIndex(bigKeyBo.getIndex() + 1);
                Map<String, Double> map = r.stream().collect(Collectors.toMap(JsonUtils::toString, v -> Double.valueOf(ReflectUtil.getFieldValue(v, "id").toString())));
                String smallKey = cacheEnum.join(bigKeyBo.getIndex(), uuid);
                redisService.zAdd(smallKey, map);
                redisService.expire(smallKey, cacheEnum.getExpireSec());
                removes.addAll(r);
            }
            readyCacheList.removeAll(removes);
        }

        // 2、如果已经结束还有未缓存的记录，继续缓存
        if (!bigKeyBo.getEndFlag()) {
            return;
        }
        if (!CollectionUtils.isEmpty(readyCacheList)) {
            bigKeyBo.setIndex(bigKeyBo.getIndex() + 1);
            String smallKey = cacheEnum.join(bigKeyBo.getIndex(), uuid);
            Map<String, Double> map = readyCacheList.stream().collect(Collectors.toMap(JsonUtils::toString, v -> Double.valueOf(ReflectUtil.getFieldValue(v, "id").toString())));
            redisService.zAdd(smallKey, map);
            redisService.expire(smallKey, cacheEnum.getExpireSec());
            readyCacheList.clear();
        }

        // 3、如果已经结束，设置缓存key
        bigKeyBo.setIndex(bigKeyBo.getIndex() - 1);
        String value = uuid + "#" + bigKeyBo.getIndex().toString();
        redisService.set(cacheEnum.join(), value, cacheEnum.getExpireSec());
        bigKeyBo.setKeyName(cacheEnum.join());
        bigKeyBo.setKeyValue(value);
    }


}
