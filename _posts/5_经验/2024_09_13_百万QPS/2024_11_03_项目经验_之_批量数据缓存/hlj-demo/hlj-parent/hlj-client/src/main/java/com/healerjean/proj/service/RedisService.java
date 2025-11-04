package com.healerjean.proj.service;

import java.util.Map;
import java.util.Set;

/**
 * RedisService 缺失实现类
 *
 * @author zhangyujin
 * @date 2023/5/26  11:29
 */
public interface RedisService {

    /**
     * 写入缓存数据
     *
     * @param key      缓存Key
     * @param value    缓存Value
     * @param timeout  缓存时间
     */
    boolean set(String key, String value, long timeout);

    /**
     * 获取缓存数据
     *
     * @param key 缓存Key
     * @return 缓存Value
     */
    String get(String key);

    /**
     * 将多个 member 元素及其 score 值加入到有序集 key 当中。 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值。
     * 时间复杂度 O(log(N))
     * Params:
     * key – key scoreMembers – 多个member、score的集合
     * Returns:
     * Long ,成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
     *
     * @param key    key
     * @param member 成员
     */
    Long zAdd(String key, Map<String, Double> member);

    /**
     * 返回有序集合 key 中，指定区间内的成员。
     * 下标参数 start 和 stop 都以 0 为底，以 0 表示有序集第一个成员，
     * 以 1 表示有序集第二个成员，以此类推。可以使用负数下标，
     * 以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     *
     * @param start 集合元素起始位置
     * @param end   集合元素结束位置
     * @return 指定区间内有序集成员的列表。
     */
    Set<String> zRange(String key, long start, long end);

    /**
     * 返回有序集 key 的基数。
     * 时间复杂度: O(1)
     *
     * @param key key
     * @return key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     */
    Long zCard(String key);


    /**
     * 为给定 key 设置过期时间(s)
     * 时间复杂度：O(1)
     *
     * @param key     key
     * @param seconds seconds
     * @return 设置成功返回 1 。 当 key 不存在，返回 0
     */
    boolean expire(String key, int seconds);

    /**
     * 加锁
     *
     * @param key      加锁的Key
     * @param threadId 标识设备
     * @param time     加锁时间
     * @return 加锁结果
     */
    boolean lock(String key, String threadId, long time);

    /**
     * 解锁
     *
     * @param key      加锁的Key
     * @param threadId 标识设备
     * @return 解锁结果
     */
    boolean unLock(String key, String threadId);

    void setBit(String bitKey, long bucketOffset, boolean value);


    Boolean getBit(String bitKey, long bucketOffset);
}
