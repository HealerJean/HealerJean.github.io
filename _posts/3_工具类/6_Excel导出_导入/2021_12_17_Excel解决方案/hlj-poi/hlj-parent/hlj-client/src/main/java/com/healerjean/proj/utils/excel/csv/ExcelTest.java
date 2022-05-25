package com.healerjean.proj.utils.excel.csv;

import com.healerjean.proj.dto.excel.read.UrlWhiteConfigExcel;
import com.healerjean.proj.utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/5/20  10:06.
 * @description
 */
public class ExcelTest {


    @Test
    public void test() throws IOException {


        InputStream inputStream = FileUtils.readeInputStreamByUrl(null);

        // List<UrlWhiteConfigExcel> list = ExcelReaderUtils.readerCsvInputStream(inputStream, UrlWhiteConfigExcel.class);
        List<UrlWhiteConfigExcel> list = ExcelReaderUtils.readerExcel(inputStream, UrlWhiteConfigExcel.class);

        System.out.println(list);

    }
}
