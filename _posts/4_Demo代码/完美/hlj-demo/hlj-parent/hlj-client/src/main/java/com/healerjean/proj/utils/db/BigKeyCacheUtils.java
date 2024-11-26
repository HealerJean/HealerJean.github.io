package com.healerjean.proj.utils.db;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.healerjean.proj.common.contants.RedisConstants;
import com.healerjean.proj.data.bo.BigKeyDataBO;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.JsonUtils;
import com.healerjean.proj.utils.SpringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public static <R> BigKeyDataBO<R> cacheDataByCursorId(Function<IdQueryBO, List<R>> function, long dbSize, RedisConstants.BigCacheEnum bigCacheEnum) {
        BigKeyDataBO<R> bigKey = new BigKeyDataBO<>();
        bigKey.setIndex(1L);
        bigKey.setEndFlag(false);
        bigKey.setCacheEnum(bigCacheEnum);
        IdQueryBO idQuery = new IdQueryBO(0L, dbSize);
        while (true) {
            List<R> dbList = function.apply(idQuery);
            if (CollectionUtils.isEmpty(dbList)) {
                bigKey.setEndFlag(true);
                cacheBigDayData(bigKey);
                break;
            }
            idQuery.setMinId(idQuery.getMaxId());

            List<R> readyList = Optional.ofNullable(bigKey.getReadyList()).orElse(new ArrayList<>());
            readyList.addAll(dbList);
            bigKey.setReadyList(readyList);
            bigKey.getData().addAll(readyList);
            cacheBigDayData(bigKey);
        }
        return bigKey;
    }


    /**
     * 大数据量-IdSize查询全部
     */
    public static <R> void cacheBigDayData(BigKeyDataBO<R> bigKeyBo) {
        long uuid = System.currentTimeMillis();
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        RedisConstants.BigCacheEnum cacheEnum = bigKeyBo.getCacheEnum();

        int size = cacheEnum.getValueSize();
        List<R> readyList = bigKeyBo.getReadyList();
        while (readyList.size() >= size) {
            List<List<R>> partition = Lists.partition(readyList, size);
            List<R> removes = Lists.newArrayList();
            for (List<R> r : partition) {
                if (r.size() < size) {
                    break;
                }
                Map<String, Double> map = r.stream().collect(Collectors.toMap(JsonUtils::toString, v -> Double.valueOf(ReflectUtil.getFieldValue(v, "id").toString())));
                String smallKey = cacheEnum.join(bigKeyBo.getIndex(), uuid);
                redisService.zAdd(smallKey, map);
                redisService.expire(smallKey, cacheEnum.getExpireSec());
                removes.addAll(r);
                bigKeyBo.setIndex(bigKeyBo.getIndex() + 1);
            }
            readyList.removeAll(removes);
        }
        if (bigKeyBo.getEndFlag() && !CollectionUtils.isEmpty(readyList)) {
            Map<String, Double> map = readyList.stream().collect(Collectors.toMap(JsonUtils::toString, v -> Double.valueOf(ReflectUtil.getFieldValue(v, "id").toString())));
            redisService.zAdd(cacheEnum.join(bigKeyBo.getIndex(), uuid), map);
            redisService.expire(cacheEnum.join(bigKeyBo.getIndex(), uuid), cacheEnum.getExpireSec());
            bigKeyBo.setIndex(bigKeyBo.getIndex() + 1);
        }

        if (bigKeyBo.getEndFlag()){
            bigKeyBo.setIndex(bigKeyBo.getIndex()-1);
            redisService.set(cacheEnum.join(), uuid + "_" + bigKeyBo.getIndex().toString(), cacheEnum.getExpireSec());
        }
    }


}
