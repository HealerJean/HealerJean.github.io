package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskExportCheckResultBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;

/**
 * ReadFileTaskHandler
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public interface ExportFileTaskHandler extends FileTaskHandler {

    /**
     * 校验导出的请求
     *
     * @return FileTaskResultBO
     */
    FileTaskExportCheckResultBO checkExportReq(FileTaskBO fileTask);

    /**
     * 执行导出任务
     *
     * @return FileTaskResultBO
     */
    FileTaskResultBO executeExportTask(FileTaskBO fileTask) throws Exception;
}
