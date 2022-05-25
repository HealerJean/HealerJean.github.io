package com.healerjean.proj.utils.excel.csv;

import com.healerjean.proj.dto.csv.CsvDemoDTO;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/3/28  17:53.
 * @description
 */
public class CsvTest {

    @Test
    public void testWrite() {
        String[] cm = {"a"};
        // String[] hm = {"HA", "HB", "HC"};
        String[] hm = {};
        List<CsvDemoDTO> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            CsvDemoDTO demo = new CsvDemoDTO();
            demo.setName("11111111111" + i);
            list.add(demo);
        }
        ExcelWriterUtils.getInstance(cm, hm).writeCsv(list, "test.csv", "/Users/healerjean/Desktop/logs/", CsvDemoDTO.class);
    }

    @Test
    public void testRead() throws FileNotFoundException {
        File file = new File("/Users/healerjean/Desktop/logs/test.csv");
        List<CsvDemoDTO> list = ExcelReaderUtils.getInstance().readerCsv(file, CsvDemoDTO.class);
        System.out.println(list.size());
    }


}
