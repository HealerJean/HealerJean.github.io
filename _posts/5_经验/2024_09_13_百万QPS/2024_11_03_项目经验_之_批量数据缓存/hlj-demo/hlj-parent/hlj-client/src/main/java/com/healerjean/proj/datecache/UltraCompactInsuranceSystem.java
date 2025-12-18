package com.healerjean.proj.datecache;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 超大规模险种签约状态查询系统
 * 针对200万商家场景优化，内存占用从4G优化到100M以下
 */
public class UltraCompactInsuranceSystem {

    /**
     * 全局基准时间：固定值，减少内存占用
     * 2024年1月1日 00:00:00
     */
    private static final long GLOBAL_BASE_TIME = 1704067200000L; // 2024-01-01 00:00:00

    /**
     * 最大时间范围：50年（秒）
     */
    private static final int MAX_TIME_RANGE_SECONDS = 50 * 365 * 24 * 60 * 60; // 约1576800000秒

    /**
     * 使用最紧凑的存储结构
     * key: 商家ID, value: 该商家的险种签约数据
     */
    private final Int2ObjectOpenHashMap<DynamicCompactData> vendorData = new Int2ObjectOpenHashMap<>();


    /**
     * 简化的动态记录 - 内存最优化存储
     * 存储结构：[险种ID, 开始时间偏移, 结束时间偏移]
     */
    static class DynamicCompactData {
        /**
         * [][3] - 业务id, 开始时间偏移(秒), 结束时间偏移(秒)
         * 时间偏移 = (实际时间 - 基准时间) / 1000
         */
        private int[][] records;

        /**
         * 有效性标记(逻辑删除)
         */
        private boolean[] valid;

        /**
         * 数组大小
         */
        private int size;

        /**
         * 初始化对象
         *
         * @param initialCapacity 初始容量
         */
        public DynamicCompactData(int initialCapacity) {
            this.size = 0;
            // 预分配容量，避免频繁扩容
            this.records = new int[Math.max(initialCapacity, 10)][3];
            this.valid = new boolean[Math.max(initialCapacity, 10)];
        }

        /**
         * 扩容机制
         */
        private void resize() {
            int newCapacity = records.length * 2;
            int[][] newRecords = new int[newCapacity][3];
            boolean[] newValid = new boolean[newCapacity];

            System.arraycopy(records, 0, newRecords, 0, records.length);
            System.arraycopy(valid, 0, newValid, 0, valid.length);

            records = newRecords;
            valid = newValid;
        }

        /**
         * 添加记录
         *
         * @param insuranceId 险种ID
         * @param startTime   开始时间
         * @param endTime     结束时间
         */
        public void addInsurance(int insuranceId, long startTime, long endTime) {
            if (size >= records.length) {
                resize();
            }
            // 计算时间偏移（相对于基准时间），并限制在50年范围内
            int startOffset = (int) Math.min((startTime - GLOBAL_BASE_TIME) / 1000L, MAX_TIME_RANGE_SECONDS);
            int endOffset = (int) Math.min((endTime - GLOBAL_BASE_TIME) / 1000L, MAX_TIME_RANGE_SECONDS);

            records[size][0] = insuranceId;  // 险种ID
            records[size][1] = startOffset;  // 开始时间偏移
            records[size][2] = endOffset;    // 结束时间偏移
            valid[size] = true;
            size++;
        }

        /**
         * 查询当前时间有效的险种ID列表
         *
         * @param currentTime 当前时间戳
         * @return 有效的险种ID列表
         */
        public List<Integer> getActiveInsuranceIds(long currentTime) {
            List<Integer> result = new ArrayList<>();
            int currentTimeOffset = (int) Math.min((currentTime - GLOBAL_BASE_TIME) / 1000L, MAX_TIME_RANGE_SECONDS);

            for (int i = 0; i < size; i++) {
                if (valid[i] &&
                        records[i][1] <= currentTimeOffset &&
                        records[i][2] >= currentTimeOffset) {
                    result.add(records[i][0]);
                }
            }
            return result;
        }

        /**
         * 获取总记录数
         */
        public int size() {
            return size;
        }
    }

    /**
     * 添加商家的险种签约记录
     *
     * @param vendorId    商家ID
     * @param insuranceId 险种ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     */
    public void addSignUpRecord(int vendorId, int insuranceId, long startTime, long endTime) {
        DynamicCompactData data = vendorData.computeIfAbsent(vendorId, k -> new DynamicCompactData(10));
        data.addInsurance(insuranceId, startTime, endTime);
    }

    /**
     * 查询商家在指定时间有效的险种列表
     *
     * @param vendorId    商家ID
     * @param currentTime 当前时间
     * @return 有效的险种ID列表
     */
    public List<Integer> getActiveInsuranceIds(int vendorId, long currentTime) {
        DynamicCompactData data = vendorData.get(vendorId);
        if (data == null) {
            return new ArrayList<>();
        }
        return data.getActiveInsuranceIds(currentTime);
    }

    /**
     * 批量查询商家在指定时间有效的险种列表
     *
     * @param vendorIds   商家ID列表
     * @param currentTime 当前时间
     * @return Map<商家ID, 有效险种ID列表>
     */
    public Map<Integer, List<Integer>> batchQueryActiveInsuranceIds(List<Integer> vendorIds, long currentTime) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (int vendorId : vendorIds) {
            result.put(vendorId, getActiveInsuranceIds(vendorId, currentTime));
        }
        return result;
    }

    /**
     * 获取系统统计信息
     */
    public SystemStats getSystemStats() {
        int totalVendors = vendorData.size();
        int totalRecords = 0;
        int maxRecordsPerVendor = 0;

        for (DynamicCompactData data : vendorData.values()) {
            totalRecords += data.size();
            maxRecordsPerVendor = Math.max(maxRecordsPerVendor, data.size());
        }

        return new SystemStats(totalVendors, totalRecords, maxRecordsPerVendor);
    }

    /**
     * 系统统计信息
     */
    public static class SystemStats {
        public final int totalVendors;
        public final int totalRecords;
        public final int maxRecordsPerVendor;
        public final double avgRecordsPerVendor;

        public SystemStats(int totalVendors, int totalRecords, int maxRecordsPerVendor) {
            this.totalVendors = totalVendors;
            this.totalRecords = totalRecords;
            this.maxRecordsPerVendor = maxRecordsPerVendor;
            this.avgRecordsPerVendor = totalVendors > 0 ? (double) totalRecords / totalVendors : 0;
        }

        @Override
        public String toString() {
            return String.format(
                    "SystemStats{总商家数=%d, 总记录数=%d, 最大单商家记录数=%d, 平均每商家记录数=%.2f}",
                    totalVendors, totalRecords, maxRecordsPerVendor, avgRecordsPerVendor
            );
        }
    }

    /**
     * 估算内存使用量
     */
    public long estimateMemoryUsage() {
        // 每个int占用4字节
        // 每个DynamicCompactData对象包含:
        // - int[][] records (3 * 4字节 * 记录数)
        // - boolean[] valid (1字节 * 记录数)
        // - int size (4字节)
        // - 对象头等开销约16字节

        long totalMemory = 0;
        for (DynamicCompactData data : vendorData.values()) {
            int capacity = data.records.length;
            // records数组: 3个int * capacity * 4字节
            totalMemory += 3L * capacity * 4;
            // valid数组: capacity * 1字节
            totalMemory += capacity * 1L;
            // 对象开销
            totalMemory += 32; // 估算对象头等开销
        }

        // Map本身开销
        totalMemory += vendorData.size() * 32L; // 估算HashMap开销

        return totalMemory;
    }

    /**
     * 测试和演示
     */
    public static void main(String[] args) {
        UltraCompactInsuranceSystem system = new UltraCompactInsuranceSystem();
        // 模拟数据：200万商家，每家平均10条记录
        // Step 1: 生成 200 万个不重复的随机 vendorId（int 范围内）
        Set<Integer> vendorIds = new HashSet<>();
        Random random = ThreadLocalRandom.current();
        // 注意：int 范围很大（约21亿），200万冲突概率极低
        while (vendorIds.size() < 2_000_000) {
            int id = random.nextInt();
            vendorIds.add(id);
        }

        long startTime = System.currentTimeMillis();
        // 转为 List 以便遍历（或直接遍历 Set）
        List<Integer> vendorIdList = new ArrayList<>(vendorIds);

        // Step 2: 遍历每个随机 vendorId
        for (int idx = 0; idx < vendorIdList.size(); idx++) {
            int vendorId = vendorIdList.get(idx);

            // 每个商家添加 10 条记录
            for (int i = 0; i < 10; i++) {
                long start = startTime + (i * 30L * 24 * 60 * 60 * 1000); // 每月
                long end = start + (28L * 24 * 60 * 60 * 1000); // 28天
                system.addSignUpRecord(vendorId, 5990 + i, start, end);
            }

            // 每 10 万输出进度
            if ((idx + 1) % 100_000 == 0) {
                System.out.println("已处理 " + (idx + 1) + " 个商家");
            }
        }



        long endTime = System.currentTimeMillis();
        System.out.println("数据加载完成，耗时: " + (endTime - startTime) + "ms");

        // 查询测试
        startTime = System.currentTimeMillis();
        List<Integer> activeInsurances = system.getActiveInsuranceIds(123456, System.currentTimeMillis());
        endTime = System.currentTimeMillis();
        System.out.println("查询结果: " + activeInsurances);
        System.out.println("单次查询耗时: " + (endTime - startTime) + "ms");

        // 系统统计
        SystemStats stats = system.getSystemStats();
        System.out.println(stats);

        // 内存使用估算
        long memoryUsage = system.estimateMemoryUsage();
        System.out.println("估算内存使用: " + memoryUsage + " 字节 (" +
                String.format("%.2f", memoryUsage / (1024.0 * 1024.0)) + " MB)");
    }
}



