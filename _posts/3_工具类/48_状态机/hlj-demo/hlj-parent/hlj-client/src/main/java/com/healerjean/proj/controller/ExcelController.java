package com.healerjean.proj.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.google.common.collect.Lists;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.excel.UserDemoExcel;
import com.healerjean.proj.service.UserDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

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
     * excel sheet最大行数(算标题)
     */
    public static final Integer EXCEL_SHEET_ROW_MAX_SIZE = 1000001;


    private static final String PATH = "/Users/healerjean/Desktop/data/write/";
    private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";

    private static final String WRITE_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE = PATH + "excludeOrIncludeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_INDEXWRITE_EXCEL_RESULT_FILE = PATH + "indexWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_COMPLEXHEADWRITE_EXCEL_RESULT_FILE = PATH + "complexHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE = PATH + "repeatedWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_CONVERTERWRITE_EXCEL_RESULT_FILE = PATH + "converterWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_IMAGEWRITE_EXCEL_RESULT_FILE = PATH + "imageWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_IMAGEWRITE_IMAGE_FILE = "/Users/healerjean/Desktop/data/write/image/img.jpg";

    private static final String WRITE_DEMO_EXCEL_RESULT_FILE = PATH + "demo.xlsx";
    private static final String WRITE_TEMPLATEWRITE_EXCEL_RESULT_FILE = PATH + "templateWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_WIDTHANDHEIGHTWRITE_EXCEL_RESULT_FILE = PATH + "widthAndHeightWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_ANNOTATIONSTYLEWRITE_EXCEL_RESULT_FILE = PATH + "annotationStyleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_MERGEWRITE_EXCEL_RESULT_FILE = PATH + "mergeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_LONGESTMATCHCOLUMNWIDTHWRITE_EXCEL_RESULT_FILE = PATH + "longestMatchColumnWidthWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_DYNAMICHEADWRITE_EXCEL_RESULT_FILE = PATH + "dynamicHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_NOMODELWRITE_EXCEL_RESULT_FILE = PATH + "noModelWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;

    @ApiOperation(value = "导出")
    @PostMapping("export")
    @ResponseBody
    public BaseRes<List<UserDemoExcel>> export() {
        String newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));

        ExcelWriter excelWriter = EasyExcel.write(newFileName, UserDemoExcel.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).autoCloseStream(false).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(1, "Sheet1").build();

        AtomicLong count = new AtomicLong();
        CompletionService<List<UserDemoExcel>> completionService = new ExecutorCompletionService(Executors.newFixedThreadPool(10));
        List<Future<List<UserDemoExcel>>> futures = userDemoService.queryFutureAll(completionService, new UserDemoQueryBO());
        List<UserDemoExcel> all = Lists.newArrayList();
        for (int i = 0;  i< futures.size() ; i++) {
            try {
                Future<List<UserDemoExcel>> future = completionService.take();
                List<UserDemoExcel> userDemos = future.get();
                if (CollectionUtils.isEmpty(userDemos)) {
                    continue;
                }
                writeSheet.setSheetNo((int) (count.addAndGet(userDemos.size()) /2 + 1));
                writeSheet.setSheetName("Sheet" + writeSheet.getSheetNo());
                excelWriter.write(userDemos, writeSheet);
                all.addAll(future.get());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (excelWriter != null) {
            excelWriter.finish();
        }
        return BaseRes.buildSuccess(all);
    }
}
