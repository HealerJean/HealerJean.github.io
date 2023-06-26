package com.healerjean.proj.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ExcelReaderUtils
 *
 * @author zhangyujin
 * @date 2023/6/21$  15:37$
 */
@Slf4j
public final class ExcelReaderUtils {

    /**
     * readerCsvFromFile 跳过第一行
     *
     * @param file  文件
     * @param excel {@link  ExcelEnums.Excel}
     * @param <E>   E
     * @return List<E>
     */
    public static <E> List<E> readerExcelFromFile(File file, ExcelEnums.Excel excel) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return readerExcelFromInputStream(fileInputStream, excel);
        } catch (Exception e) {
            log.error("[ExcelReaderUtils#readerExcelFromFile] error", e);
            return Collections.emptyList();
        }
    }


    /**
     * 处理Excel数据
     *
     * @param inputStream InputStream
     * @param excel       ExcelEnums.Excel
     * @param <E>         E
     * @return List<E>
     */
    public static <E> List<E> readerExcelFromInputStream(InputStream inputStream, ExcelEnums.Excel excel) {
        List<E> list = new ArrayList<>();
        EasyExcel.read(inputStream, excel.getDataClass(), new ReadListener<E>() {
            @Override
            public void invoke(E data, AnalysisContext context) {
                list.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet().doRead();
        return list;
    }

    /**
     * 处理Excel数据
     *
     * @param url   文件HTTP下载链接
     * @param excel ExcelEnums.Excel
     * @param <E>   E
     * @return List<E>
     */
    public static <E> List<E> readerExcelFromUrl(String url, ExcelEnums.Excel excel) {
        try {
            URL httpUrl = new URL(url);
            URLConnection urlConnection = httpUrl.openConnection();
            return readerExcelFromInputStream(urlConnection.getInputStream(), excel);
        } catch (Exception e) {
            log.error("[ExcelReaderUtils#readerExcelFromUrl] error, url:{}", url, e);
            return Collections.emptyList();
        }
    }

}
