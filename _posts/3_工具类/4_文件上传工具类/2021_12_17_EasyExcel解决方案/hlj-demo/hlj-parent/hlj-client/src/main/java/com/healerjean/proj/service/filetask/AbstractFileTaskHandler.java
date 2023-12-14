package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;
import com.healerjean.proj.enums.FileTaskEnum;
import com.healerjean.proj.service.FileTaskService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Objects;


/**
 * 抽象任务处理
 *
 * @author zhangyujin
 * @date 2023-12-13 02:12:26
 */
@Slf4j
public abstract class AbstractFileTaskHandler implements FileTaskHandler {

    /**
     * fileTaskService
     */
    @Resource
    private FileTaskService fileTaskService;

    /**
     * businessTypeEnum
     */
    private final FileTaskEnum.BusinessTypeEnum businessTypeEnum;

    /**
     * taskTypeEnum
     */
    private final FileTaskEnum.TaskTypeEnum taskTypeEnum;

    /**
     * 抽象类
     *
     * @param businessTypeEnum businessTypeEnum
     */
    public AbstractFileTaskHandler(FileTaskEnum.TaskTypeEnum taskTypeEnum, FileTaskEnum.BusinessTypeEnum businessTypeEnum) {
        this.businessTypeEnum = businessTypeEnum;
        this.taskTypeEnum = taskTypeEnum;

    }

    /**
     * 是否匹配 FileTaskHandler
     *
     * @param businessType businessType
     * @return boolean
     */
    @Override
    public boolean match(String businessType) {
        return businessTypeEnum.getCode().equals(businessType);
    }

    /**
     * handler
     *
     * @param taskId 任务id
     */
    @Override
    public void handler(String taskId) throws Exception {
        FileTaskQueryBO queryBO = new FileTaskQueryBO();
        queryBO.setTaskId(taskId);
        FileTaskBO fileTaskBO = fileTaskService.queryFileTaskSingle(queryBO);
        if (fileTaskBO == null) {
            throw new RuntimeException("任务不存在!" + taskId);
        }
        FileTaskResultBO taskResult = executeTask(fileTaskBO);
        if (Objects.isNull(taskResult)) {
            throw new RuntimeException("任务异常!" + taskId);
        }
        FileTaskEnum.TaskStatusEnum taskStatusEnum = taskResult.getTaskStatusEnum();
        if (Objects.isNull(taskStatusEnum)) {
            throw new RuntimeException("任务Bug异常!" + taskId);
        }
        switch (taskStatusEnum) {
            case COMPLETED:
                log.info("taskTypeEnum:{}, businessTypeEnum:{}, taskId:{}, success", taskTypeEnum, businessTypeEnum, taskId);
                fileTaskService.updateFileTask(taskResult.getUpdateFileTaskBO().setTaskStatus(FileTaskEnum.TaskStatusEnum.COMPLETED.getCode()).setTaskId(fileTaskBO.getTaskId()));
                break;
            case FAIL:
                log.info("taskTypeEnum:{}, businessTypeEnum:{}, taskId:{}, fail, errorMsg:{}", taskTypeEnum, businessTypeEnum, taskId, taskResult.getResultDesc());
                fileTaskService.updateFileTask(taskResult.getUpdateFileTaskBO().setTaskStatus(FileTaskEnum.TaskStatusEnum.FAIL.getCode()).setTaskId(fileTaskBO.getTaskId()).setResultMessage(taskResult.getResultDesc()));
                break;
            default:
                throw new RuntimeException("任务异常!" + taskId);
        }
    }


    /**
     * 执行任务，等待子类实现
     *
     * @param fileTaskBO fileTaskBO
     * @return FileTaskResultBO
     */
    abstract FileTaskResultBO executeTask(FileTaskBO fileTaskBO) throws Exception;

}
