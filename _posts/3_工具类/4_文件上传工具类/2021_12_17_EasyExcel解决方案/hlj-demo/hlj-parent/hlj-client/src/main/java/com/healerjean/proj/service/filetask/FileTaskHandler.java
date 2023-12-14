package com.healerjean.proj.service.filetask;

/**
 * FileTaskHandler
 *
 * @author zhangyujin
 * @date 2023/6/26$  19:59$
 */
public interface FileTaskHandler {


    /**
     * handler是否匹配
     *
     * @param businessType businessType
     * @return boolean
     */
    boolean match(String businessType);

    /**
     * 执行任务
     *
     * @param taskId 任务id
     */
    void handler(String taskId) throws Exception;
}
