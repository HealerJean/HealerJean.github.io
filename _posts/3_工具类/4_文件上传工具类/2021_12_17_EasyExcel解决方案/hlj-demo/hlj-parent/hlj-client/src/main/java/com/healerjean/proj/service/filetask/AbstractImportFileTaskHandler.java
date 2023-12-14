package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskImportCheckResultBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;
import com.healerjean.proj.enums.FileTaskEnum;

/**
 * 导入抽象控制器
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public abstract class AbstractImportFileTaskHandler extends AbstractFileTaskHandler implements ImportFileTaskHandler {

    /**
     * 抽象类
     *
     * @param taskTypeEnum     taskTypeEnum
     * @param businessTypeEnum businessTypeEnum
     */
    public AbstractImportFileTaskHandler(FileTaskEnum.TaskTypeEnum taskTypeEnum, FileTaskEnum.BusinessTypeEnum businessTypeEnum) {
        super(taskTypeEnum, businessTypeEnum);
    }

    /**
     * executeTask
     * 1、校验上传的excel
     * 2、导入校验通过的数据
     * 3、上传校验完成excel
     *
     * @param fileTaskBo fileTaskBO
     * @return {@link FileTaskResultBO}
     */
    @Override
    public FileTaskResultBO executeTask(FileTaskBO fileTaskBo) throws Exception {
        // 1、校验上传的excel
        FileTaskImportCheckResultBO checkResult = checkImportExcel(fileTaskBo);
        if (Boolean.FALSE.equals(checkResult.getCheckFlag())) {
            return FileTaskResultBO.fail(new FileTaskBO().setTaskId(fileTaskBo.getTaskId()), checkResult.getResultDesc());
        }
        // 2、导入校验通过的数据
        FileTaskResultBO fileTaskResult = executeImportTask(fileTaskBo, checkResult);

        // 3、上传校验完成excel
        fileTaskResult.setResultDesc(checkResult.getResultDesc());
        return fileTaskResult;
    }


}
