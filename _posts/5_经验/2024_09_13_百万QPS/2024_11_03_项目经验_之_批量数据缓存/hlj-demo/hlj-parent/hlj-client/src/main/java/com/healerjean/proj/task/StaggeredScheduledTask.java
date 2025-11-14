package com.healerjean.proj.task;

import com.healerjean.proj.task.config.InstanceIdentityConfig;
import com.healerjean.proj.task.config.TaskScheduleConfig;
import com.healerjean.proj.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 错峰定时任务调度器
 * 多实例部署时，通过 instanceId 分配批次，实现错峰执行，避免集体压垮下游服务
 *
 * @author zhangyujin
 * @date 2025/11/11
 */
@Slf4j
@Component
public class StaggeredScheduledTask {

    @Resource
    private TaskScheduleConfig taskScheduleConfig;

    @Resource
    private InstanceIdentityConfig instanceIdentityConfig;

    private ScheduledFuture<?> scheduledFuture;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "staggered-task-scheduler");
        t.setDaemon(true);
        return t;
    });

    @PostConstruct
    public void init() {
        if (!taskScheduleConfig.isEnabled()) {
            log.info("✅ 定时任务已禁用");
            return;
        }

        // ========== 启动时立即执行一次（失败则启动失败）==========
        String instanceId = instanceIdentityConfig.getInstanceId();
        executePeriodicTask(instanceId, true);

        // ========== 计算错峰偏移并启动周期调度 ==========
        long intervalSeconds = taskScheduleConfig.getIntervalSeconds();
        int totalBatches = taskScheduleConfig.getTotalBatches();
        int batch = Math.abs(instanceId.hashCode()) % totalBatches;
        long offsetSeconds = totalBatches > 0 ? batch * (intervalSeconds / totalBatches) : 0;
        String offsetHumanReadable = DateUtils.ExtTool.formatDuration(offsetSeconds);
        String intervalHumanReadable = DateUtils.ExtTool.formatDuration(intervalSeconds);
        log.info("📊 实例 [{}] 分配到批次: {}, 调度周期: {}, 首次延迟: {}",
                instanceId, batch, intervalHumanReadable, offsetHumanReadable);
        scheduledFuture = scheduler.scheduleWithFixedDelay(
                () -> executePeriodicTask(instanceId, false),
                offsetSeconds,
                intervalSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * 周期性任务执行入口（异常被捕获，不影响调度）
     */
    private void executePeriodicTask(String instanceId, boolean isStartup) {
        long start = System.currentTimeMillis();
        try {
            log.info("⏳【{}】开始执行周期性错峰任务（线程: {}）...", instanceId, Thread.currentThread().getName());
            doYourRefreshWork();

            logTaskCompletion(instanceId, start, taskScheduleConfig.getIntervalSeconds());

        } catch (Exception e) {
            if (isStartup) {
                long costMs = System.currentTimeMillis() - start;
                log.error("💥 启动阶段任务执行失败（兜底）| 实例: {} | 已运行: {} ms", instanceId, costMs, e);
                throw e;
            }
            logTaskFailure(instanceId, start, taskScheduleConfig.getIntervalSeconds(), e);
        }
    }

    /**
     * 记录任务成功日志
     */
    private void logTaskCompletion(String instanceId, long startTimeMillis, long intervalSeconds) {
        long costMs = System.currentTimeMillis() - startTimeMillis;
        long nextExecuteTimeMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(intervalSeconds);
        String nextTimeStr = DateUtils.DateTool.toStr(new Date(nextExecuteTimeMillis), "yyyy-MM-dd HH:mm:ss");
        String delayDesc = DateUtils.ExtTool.formatDuration(intervalSeconds);

        log.info("✅【{}】周期任务执行成功 | 耗时: {} ms | 下次执行: {}（{}后）",
                instanceId, costMs, nextTimeStr, delayDesc);
    }

    /**
     * 记录任务失败日志
     */
    private void logTaskFailure(String instanceId, long startTimeMillis, long intervalSeconds, Exception e) {
        long costMs = System.currentTimeMillis() - startTimeMillis;
        long nextExecuteTimeMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(intervalSeconds);
        String nextTimeStr = DateUtils.DateTool.toStr(new Date(nextExecuteTimeMillis), "yyyy-MM-dd HH:mm:ss");
        String delayDesc = DateUtils.ExtTool.formatDuration(intervalSeconds);

        log.error("❌【{}】周期任务执行失败 | 已运行: {} ms | 错误原因:", instanceId, costMs, e);
        log.warn("⚠️【{}】调度将继续，下次执行时间: {}（{}后）", instanceId, nextTimeStr, delayDesc);
    }

    /**
     * 核心业务逻辑 —— 请替换为实际刷新/同步操作
     * <p>
     * 注意：
     * - 启动时调用此方法若抛异常，会导致 Spring 启动失败
     * - 周期调度中调用此方法的异常会被捕获并记录
     */
    private void doYourRefreshWork() {
        // 示例：调用外部接口、刷新缓存、更新本地状态等
        // restTemplate.getForObject("https://api.example.com/sync", Void.class);
    }

    @PreDestroy
    public void destroy() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
        scheduler.shutdownNow();
        log.info("🛑 错峰定时任务调度器已关闭");
    }
}