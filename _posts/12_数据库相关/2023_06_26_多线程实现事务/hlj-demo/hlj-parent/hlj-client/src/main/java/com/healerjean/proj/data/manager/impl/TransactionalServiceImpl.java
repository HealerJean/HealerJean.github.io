package com.healerjean.proj.data.manager.impl;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.common.OwnerTransactionContext;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.manager.TransactionalService;
import com.healerjean.proj.service.UserDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangyujin
 * @date 2023/6/26$  11:00$
 */
@Slf4j
@Service
public class TransactionalServiceImpl implements TransactionalService {


    /**
     * transactionManager
     */
    @Resource
    private PlatformTransactionManager transactionManager;

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;

    /**
     * 验证事务提交
     */
    @Override
    public void submit() {
        OwnerTransactionContext ownerTransactionContext = OwnerTransactionContext.initInstance(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (int i = 1; i <= 10; i++) {
                    executorService.submit(saveUserDemo(ownerTransactionContext, i + "", false));
                }
            }
        });

        CountDownLatch ownerCountDownLatch = ownerTransactionContext.getOwnerCountDownLatch();
        Vector<Throwable> errorMsgs = ownerTransactionContext.getErrorMsgs();
        try {
            ownerCountDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!CollectionUtils.isEmpty(errorMsgs)){
            log.error("[TransactionalService#rollback] errorMsgs:{}", JSON.toJSONString(errorMsgs));
        }
    }

    /**
     * 验证事务回滚
     */
    @Override
    public void rollback() {
        OwnerTransactionContext ownerTransactionContext = OwnerTransactionContext.initInstance(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (int i = 1; i <= 10; i++) {
                    executorService.submit(saveUserDemo(ownerTransactionContext, i + "", true));
                }
            }
        });

        CountDownLatch ownerCountDownLatch = ownerTransactionContext.getOwnerCountDownLatch();
        Vector<Throwable> errorMsgs = ownerTransactionContext.getErrorMsgs();
        try {
            ownerCountDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!CollectionUtils.isEmpty(errorMsgs)){
            log.error("[TransactionalService#rollback] errorMsgs:{}", JSON.toJSONString(errorMsgs));
        }
    }


    /**
     * saveUserDemo
     *
     * @param name name
     * @return Runnable
     */
    private Callable<Boolean> saveUserDemo(OwnerTransactionContext ownerTransactionContext, String name, Boolean rollbackFlag) {
        return () -> {
            CountDownLatch ownerCountDownLatch = ownerTransactionContext.getOwnerCountDownLatch();
            Vector<Throwable> errorMsgs = ownerTransactionContext.getErrorMsgs();

            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
            defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
            TransactionStatus transactionStatus = transactionManager.getTransaction(defaultTransactionDefinition);
            try {
                UserDemoBO userDemoBO = new UserDemoBO();
                userDemoBO.setName(name);
                userDemoBO.setAge(0);
                userDemoBO.setPhone("123");
                userDemoBO.setEmail("1233");
                userDemoBO.setStartTime(LocalDateTime.now());
                userDemoBO.setEndTime(LocalDateTime.now());
                userDemoBO.setValidFlag(1);
                userDemoService.saveUserDemo(userDemoBO);
                if (Boolean.TRUE.equals(rollbackFlag) && "4".equals(name)) {
                   throw new RuntimeException("报错，希望回滚");
                }
            } catch (Exception e) {
                log.error("[TransactionalService#saveUserDemo]", e);
                errorMsgs.add(e);
            } finally {
                ownerCountDownLatch.countDown();
            }


            // 等待所有线程执行完毕
            ownerCountDownLatch.await();

            if (!CollectionUtils.isEmpty(errorMsgs)) {
                log.info("线程:{}检测到异常,事务回滚", Thread.currentThread().getName());
                transactionManager.rollback(transactionStatus);
            } else {
                log.info("线程:{}正常执行完毕,事务提交", Thread.currentThread().getName());
                transactionManager.commit(transactionStatus);
            }
            return errorMsgs.size()== 0;
        };
    }


}
