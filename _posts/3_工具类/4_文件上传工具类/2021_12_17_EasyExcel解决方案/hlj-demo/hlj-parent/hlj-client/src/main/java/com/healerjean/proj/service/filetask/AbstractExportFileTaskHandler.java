package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskExportCheckResultBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;
import com.healerjean.proj.enums.FileTaskEnum;

/**
 * 导出抽象控制器
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public abstract class AbstractExportFileTaskHandler extends AbstractFileTaskHandler implements ExportFileTaskHandler {


    /**
     * 抽象类
     *
     * @param taskTypeEnum     taskTypeEnum
     * @param businessTypeEnum businessTypeEnum
     */
    public AbstractExportFileTaskHandler(FileTaskEnum.TaskTypeEnum taskTypeEnum, FileTaskEnum.BusinessTypeEnum businessTypeEnum) {
        super(taskTypeEnum, businessTypeEnum);
    }

    /**
     * 执行任务，等待子类实现
     *
     * @param fileTaskBo fileTaskBO
     * @return FileTaskResultBO
     */
    public FileTaskResultBO executeTask(FileTaskBO fileTaskBo) throws Exception {
        FileTaskExportCheckResultBO checkResult = checkExportReq(fileTaskBo);
        if (Boolean.FALSE.equals(checkResult.getCheckFlag())) {
            return FileTaskResultBO.fail(new FileTaskBO().setTaskId(fileTaskBo.getTaskId()), checkResult.getErrorMsg());
        }
        return executeExportTask(fileTaskBo);
    }

}
