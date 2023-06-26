package com.healerjean.proj.data.manager;

/**
 * TransactionalService
 * @author zhangyujin
 * @date 2023/6/26$  10:59$
 */
public interface TransactionalService {

    /**
     * 验证事务回滚
     */
    void rollback();


    /**
     * 验证事务提交
     */
    void submit();

}
