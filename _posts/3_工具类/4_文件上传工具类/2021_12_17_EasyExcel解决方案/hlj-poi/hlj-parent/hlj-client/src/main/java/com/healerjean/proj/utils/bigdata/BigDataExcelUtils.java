package com.healerjean.proj.utils.bigdata;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.healerjean.proj.utils.file.ExcelEnums;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhangyujin
 * @date 2023/6/26$  17:15$
 */
public final class BigDataExcelUtils {

    /**
     * EXCEL_SHEET_ROW_MAX_SIZE
     */
    public static final Integer EXCEL_SHEET_ROW_MAX_SIZE = 1000001; // excel sheet最大行数(算标题)


    public static <E> void writerCsv(ExcelWriter excelWriter, AtomicLong count, List<E> dataList) {
        WriteSheet writeSheet = EasyExcel.writerSheet(1, "Sheet1").build();
        writeSheet.setSheetNo((int) (count.addAndGet(dataList.size()) / EXCEL_SHEET_ROW_MAX_SIZE + 1));
        writeSheet.setSheetName("Sheet" + writeSheet.getSheetNo());
        excelWriter.write(dataList, writeSheet);
    }
}

