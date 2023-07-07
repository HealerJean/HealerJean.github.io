package com.healerjean.proj.utils.db;

import lombok.SneakyThrows;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * CommonResultHandler
 *
 * @author zhangyujin
 * @date 2023-07-07 11:07:25
 */
public abstract class CommonResultHandler<R> implements ResultHandler<R> {

    /**
     * CommonResultHandler
     */
    public CommonResultHandler() {
    }

    /**
     * handleResult
     *
     * @param resultContext resultContext
     */
    @Override
    @SneakyThrows
    public void handleResult(ResultContext<? extends R> resultContext) {
        R obj = resultContext.getResultObject();
        processing(obj);
    }

    /**
     * processing
     *
     * @param r r
     */
    public abstract void processing(R r);
}