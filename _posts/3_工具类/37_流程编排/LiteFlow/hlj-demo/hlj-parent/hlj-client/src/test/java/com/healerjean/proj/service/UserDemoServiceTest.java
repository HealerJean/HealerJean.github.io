package com.healerjean.proj.service;

import com.google.common.collect.Lists;
import com.healerjean.proj.base.BaseJunit5SpringTest;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.excel.UserDemoExcel;
import com.healerjean.proj.utils.JsonUtils;
import com.healerjean.proj.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * UserDemoServiceTest
 *
 * @author zhangyujin
 * @date 2023/7/5$  15:31$
 */
@Slf4j
@DisplayName("UserDemoServiceTest")
public class UserDemoServiceTest extends BaseJunit5SpringTest {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;


    @DisplayName("testQueryMinAndMaxId")
    @Test
    public void testQueryMinAndMaxId() {
        CompletionService<List<UserDemoExcel>> completionService = new ExecutorCompletionService(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);

        UserDemoQueryBO queryBO = new UserDemoQueryBO();
        List<Future<List<UserDemoExcel>>> futures = userDemoService.queryAllUserDemoByPoolIdSub(completionService, queryBO);
        List<UserDemoExcel> all = Lists.newArrayList();
        for (int i = 0; i < futures.size(); i++) {
            try {
                Future<List<UserDemoExcel>> future = completionService.take();
                List<UserDemoExcel> userDemos = future.get();
                if (CollectionUtils.isEmpty(userDemos)) {
                    continue;
                }
                all.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("[testQueryMinAndMaxId] res:{}", JsonUtils.toString(all));
    }


    @DisplayName("testQueryUserDemoByLimit")
    @Test
    public void testQueryUserDemoByLimit() {
        CompletionService<List<UserDemoExcel>> completionService = new ExecutorCompletionService(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);

        UserDemoQueryBO queryBO = new UserDemoQueryBO();
        List<Future<List<UserDemoExcel>>> futures = userDemoService.queryAllUserDemoByPoolLimit(completionService, queryBO);
        List<UserDemoExcel> all = Lists.newArrayList();
        for (int i = 0; i < futures.size(); i++) {
            try {
                Future<List<UserDemoExcel>> future = completionService.take();
                List<UserDemoExcel> userDemos = future.get();
                if (CollectionUtils.isEmpty(userDemos)) {
                    continue;
                }
                all.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("[testQueryUserDemoByLimit] res:{}", JsonUtils.toString(all));
    }


    @DisplayName("testQueryUserDemoByStream")
    @Test
    public void testQueryUserDemoByStream() {
        UserDemoQueryBO queryBO = new UserDemoQueryBO();
        List<UserDemoBO> all = userDemoService.queryAllUserDemoByStream(queryBO);
        log.info("[testQueryUserDemoByStream] res:{}", JsonUtils.toString(all));
    }

}
