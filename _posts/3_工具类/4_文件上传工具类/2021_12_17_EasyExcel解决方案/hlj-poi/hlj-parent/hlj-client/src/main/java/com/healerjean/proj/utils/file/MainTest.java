package com.healerjean.proj.utils.file;

import com.healerjean.proj.dto.csv.DemoExcel;
import com.healerjean.proj.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/3/28  17:53.
 * @description
 */
@Slf4j
public class MainTest {

    @Test
    public void testCsvWrite() {
        List<DemoExcel> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            DemoExcel demo = new DemoExcel();
            demo.setName("11111111111" + i);
            list.add(demo);
        }
        String filePath = CsvWriterUtils.writerCsv(list, "test.csv", ExcelEnums.Excel.DEMO_EXCEL);
        log.info("[CsvWriterUtils#writerCsv] filePath:{}", filePath);
    }

    @Test
    public void testCsvRead(){
        File file = new File("/Users/healerjean/Desktop/logs/demo_excel/test.csv");
        List<DemoExcel> list = CsvReaderUtils.readerCsvFromFile(file, ExcelEnums.Excel.DEMO_EXCEL);
        log.info("[CsvWriterUtils#readerCsvFromFile] size:{}, lineOne:{}", list.size(), JsonUtils.toJsonString(list.get(0)));
    }


    @Test
    public void testExcelWrite() {
        List<DemoExcel> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            DemoExcel demo = new DemoExcel();
            demo.setName("11111111111" + i);
            list.add(demo);
        }
        String filePath = ExcelWriterrUtils.writerExcel(list, "test.xlsx", ExcelEnums.Excel.DEMO_EXCEL);
        log.info("[ExcelWriterrUtils#writerExcel] filePath:{}", filePath);
    }

    @Test
    public void testExcelRead() {
        File file = new File("/Users/healerjean/Desktop/logs/demo_excel/test.xlsx");
        List<DemoExcel> list = ExcelReaderUtils.readerExcelFromFile(file, ExcelEnums.Excel.DEMO_EXCEL);
        log.info("[CsvTest#readerExcelFromFile] size:{}, lineOne:{}", list.size(), JsonUtils.toJsonString(list.get(0)));
    }


}
