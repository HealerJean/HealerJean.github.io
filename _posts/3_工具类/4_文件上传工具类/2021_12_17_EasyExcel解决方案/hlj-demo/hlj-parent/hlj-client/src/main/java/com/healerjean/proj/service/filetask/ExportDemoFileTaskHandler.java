package com.healerjean.proj.service.filetask;

import com.healerjean.proj.data.bo.*;
import com.healerjean.proj.data.converter.UserDemoConverter;
import com.healerjean.proj.data.excel.UserDemoExportExcel;
import com.healerjean.proj.data.req.UserDemoQueryReq;
import com.healerjean.proj.enums.ExcelEnum;
import com.healerjean.proj.enums.FileTaskEnum;
import com.healerjean.proj.service.BigDataService;
import com.healerjean.proj.utils.JsonUtils;
import com.healerjean.proj.utils.ThreadPoolUtils;
import com.healerjean.proj.utils.file.ExcelWriteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * 导出Demo 控制器
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Slf4j
@Service
public class ExportDemoFileTaskHandler extends AbstractExportFileTaskHandler {


    /**
     * bigDataService
     */
    @Resource
    private BigDataService bigDataService;


    /**
     * 抽象类
     */
    public ExportDemoFileTaskHandler() {
        super(FileTaskEnum.TaskTypeEnum.EXPORT, FileTaskEnum.BusinessTypeEnum.EXPORT_DEMO);
    }

    /**
     * 校验请求结果
     *
     * @param fileTask fileTask
     * @return FileTaskResultBO
     */
    @Override
    public FileTaskExportCheckResultBO checkExportReq(FileTaskBO fileTask) {
        // todo zhangyujin 不验证，直接通过
        // UserDemoQueryReq fileDemoExportReq = JsonUtils.toObject(fileTask.getBusinessData(), UserDemoQueryReq.class);
        // if (StringUtils.isBlank(fileDemoExportReq.getName())) {
        //     return FileTaskCheckResultBO.fail("name不能为空");
        // }
        return FileTaskExportCheckResultBO.success();
    }

    /**
     * 执行任务
     *
     * @param fileTask fileTask
     * @return {@link FileTaskResultBO}
     */
    @Override
    public FileTaskResultBO executeExportTask(FileTaskBO fileTask) throws Exception {
        UserDemoQueryReq req = JsonUtils.toObject(fileTask.getBusinessData(), UserDemoQueryReq.class);
        UserDemoQueryBO queryBO = UserDemoConverter.INSTANCE.covertUserDemoQueryReqToBo(req);

        // 大数据量-线程池limit查询
        CompletionService<List<UserDemoBO>> completionService = new ExecutorCompletionService<>(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);
        List<Future<List<UserDemoBO>>> futures = bigDataService.queryAllUserDemoByPoolLimit(completionService, queryBO);
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.instance(ExcelEnum.ExcelObjEnum.EXPORT_EXCEL_USER_DEMO);
        for (int i = 0; i < futures.size(); i++) {
            Future<List<UserDemoBO>> future = completionService.take();
            List<UserDemoBO> userDemos = future.get();
            if (CollectionUtils.isEmpty(userDemos)) {
                continue;
            }
            List<UserDemoExportExcel> excels = UserDemoConverter.INSTANCE.covertUserDemoPoToExcelList(userDemos);
            excelWriteUtils.write(excels);
        }
        excelWriteUtils.close();

        // 大数据量-IdSize查询全部
        // List<UserDemoBO> queryAllUserDemoByIdSize = bigDataService.queryAllUserDemoByIdSize(queryBO);
        // ExcelWriteHolder.instance(ExcelEnum.ExcelObjEnum.EXPORT_EXCEL_USER_DEMO).write(UserDemoConverter.INSTANCE.covertUserDemoPoToExcelList(queryAllUserDemoByIdSize)).close();

        // 大数据量-分页查询全部
        // List<UserDemoBO> queryAllUserDemoByLimit = bigDataService.queryAllUserDemoByLimit(queryBO);
        // ExcelWriteHolder.instance(ExcelEnum.ExcelObjEnum.EXPORT_EXCEL_USER_DEMO).write(UserDemoConverter.INSTANCE.covertUserDemoPoToExcelList(queryAllUserDemoByLimit)).close();
        return FileTaskResultBO.success(new FileTaskBO().setTaskId(fileTask.getTaskId()));
    }

}
