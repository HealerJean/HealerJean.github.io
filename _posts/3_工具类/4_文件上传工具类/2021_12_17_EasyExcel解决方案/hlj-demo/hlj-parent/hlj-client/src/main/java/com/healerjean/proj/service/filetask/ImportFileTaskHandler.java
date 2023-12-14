package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskImportCheckResultBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;
import com.healerjean.proj.data.excel.Excel;

/**
 * 导入任务
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public interface ImportFileTaskHandler extends FileTaskHandler {

    /**
     * 校验导入的Excel数据
     *
     * @return FileTaskResultBO
     */
    FileTaskImportCheckResultBO checkImportExcel(FileTaskBO fileTask);

    /**
     * 执行导入任务
     *
     * @param fileTaskBO fileTaskBO
     */
    FileTaskResultBO executeImportTask(FileTaskBO fileTaskBO, FileTaskImportCheckResultBO importCheckResult);

}
