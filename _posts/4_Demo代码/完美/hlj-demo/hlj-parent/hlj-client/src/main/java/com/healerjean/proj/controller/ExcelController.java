package com.healerjean.proj.controller;

import com.healerjean.proj.service.UserDemoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ExcelController
 *
 * @author zhangyujin
 * @date 2023/6/26$  18:17$
 */
@RestController
@RequestMapping("hlj/excel")
@Api(tags = "Excel-控制器")
@Slf4j
public class ExcelController {

    /**
     * excel sheet最大行数(算标题) 1000001
     */
    public static final Integer EXCEL_SHEET_ROW_MAX_SIZE = 1;

    private static final String PATH = "/Users/healerjean/Desktop/data/write/";
    private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";
    private static final String WRITE_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;

    // @ApiOperation(value = "导出")
    // @PostMapping("export")
    // @ResponseBody
    // public BaseRes<List<UserDemoExcel>> export() {
    //     String newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    //
    //     ExcelWriter excelWriter = EasyExcel.write(newFileName, UserDemoExcel.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).autoCloseStream(false).build();
    //     WriteSheet writeSheet = EasyExcel.writerSheet(1, "Sheet1").build();
    //
    //     AtomicLong count = new AtomicLong();
    //     CompletionService<List<UserDemoExcel>> completionService = new ExecutorCompletionService(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);
    //     List<Future<List<UserDemoExcel>>> futures = userDemoService.queryAllUserDemoByPoolLimit(completionService, new UserDemoQueryBO());
    //
    //     List<UserDemoExcel> all = Lists.newArrayList();
    //     for (int i = 0; i < futures.size(); i++) {
    //         try {
    //             Future<List<UserDemoExcel>> future = completionService.take();
    //             List<UserDemoExcel> userDemos = future.get();
    //             if (CollectionUtils.isEmpty(userDemos)) {
    //                 continue;
    //             }
    //             writeSheet.setSheetNo((int) (count.addAndGet(userDemos.size()) / 2 + EXCEL_SHEET_ROW_MAX_SIZE));
    //             writeSheet.setSheetName("Sheet" + writeSheet.getSheetNo());
    //             excelWriter.write(userDemos, writeSheet);
    //             all.addAll(future.get());
    //
    //         } catch (Exception e) {
    //             throw new RuntimeException(e);
    //         }
    //     }
    //
    //     if (excelWriter != null) {
    //         excelWriter.finish();
    //     }
    //     return BaseRes.buildSuccess(all);
    // }
}
