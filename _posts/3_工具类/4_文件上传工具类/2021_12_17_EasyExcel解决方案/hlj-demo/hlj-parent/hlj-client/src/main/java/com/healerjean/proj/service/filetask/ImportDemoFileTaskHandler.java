package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskImportCheckResultBO;
import com.healerjean.proj.data.bo.FileTaskResultBO;
import com.healerjean.proj.data.excel.UserDemoImportExcel;
import com.healerjean.proj.enums.FileTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 导出Demo 控制器
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Slf4j
@Service
public class ImportDemoFileTaskHandler extends AbstractImportFileTaskHandler {


    /**
     * AbstractImportFileTaskHandler
     */
    public ImportDemoFileTaskHandler() {
        super(FileTaskEnum.TaskTypeEnum.IMPORT, FileTaskEnum.BusinessTypeEnum.IMPORt_DEMO);
    }

    /**
     * 校验上传的excel表格
     *
     * @param fileTask fileTask
     * @return FileTaskImportCheckResultBO
     */
    @Override
    public FileTaskImportCheckResultBO checkImportExcel(FileTaskBO fileTask) {
        return new FileTaskImportCheckResultBO<>();
    }

    /**
     * executeImportTask
     *
     * @param fileTaskBO        fileTaskBO
     * @param importCheckResult importCheckResult
     * @return FileTaskResultBO
     */
    @Override
    public FileTaskResultBO executeImportTask(FileTaskBO fileTaskBO, FileTaskImportCheckResultBO importCheckResult) {
        List<UserDemoImportExcel> importExcelList = importCheckResult.getImportExcelList();
        return FileTaskResultBO.success(new FileTaskBO().setTaskId(fileTaskBO.getTaskId()));
    }


}
