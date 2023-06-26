package com.healerjean.proj.utils.file;

import com.opencsv.ICSVParser;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class CsvReaderUtils {

    /**
     * readerCsvFromFile 跳过第一行
     *
     * @param file  文件
     * @param excel {@link  ExcelEnums.Excel}
     * @param <E>   E
     * @return List<E>
     */
    public static <E> List<E> readerCsvFromFile(File file, ExcelEnums.Excel excel) {
        try {
            ColumnPositionMappingStrategy<E> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(excel.getDataClass());
            mappingStrategy.setColumnMapping(excel.column());
            CsvToBean<E> build = new CsvToBeanBuilder<E>(new FileReader(file))
                    .withSkipLines(1)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(ICSVParser.DEFAULT_SEPARATOR)
                    .build();
            return build.parse();
        } catch (Exception e) {
            log.error("[ExcelReaCsvReaderUtilsderUtils#readerCsvFromFile] error", e);
            return Collections.emptyList();
        }
    }

    /**
     * readerCsvFromInputStream 跳过第一行
     *
     * @param inputStream 文件
     * @param excel       {@link  ExcelEnums.Excel}
     * @param <E>         E
     * @return List<E>
     */
    public static <E> List<E> readerCsvFromInputStream(InputStream inputStream, ExcelEnums.Excel excel) {
        try {
            ColumnPositionMappingStrategy<E> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(excel.getDataClass());
            mappingStrategy.setColumnMapping(excel.column());
            CsvToBean<E> build = new CsvToBeanBuilder<E>(new InputStreamReader(inputStream))
                    .withSkipLines(1)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(ICSVParser.DEFAULT_SEPARATOR)
                    .build();
            return build.parse();
        }catch (Exception e){
            log.error("[ExcelReaCsvReaderUtilsderUtils#readerCsvFromInputStream] error", e);
            return Collections.emptyList();
        }
    }

    /**
     * readerExcelFromUrl  跳过第一行
     *
     * @param url   文件HTTP下载链接
     * @param excel {@link  ExcelEnums.Excel}
     * @param <E>   E
     * @return List<E>
     */
    public static <E> List<E> readerExcelFromUrl(String url, ExcelEnums.Excel excel) {
        try {
            URL httpUrl = new URL(url);
            URLConnection urlConnection = httpUrl.openConnection();
            return readerCsvFromInputStream(urlConnection.getInputStream(), excel);
        } catch (Exception e) {
            log.error("[CsvReaderUtils#readerExcelFromUrl] error, url:{}", url, e);
            return Collections.emptyList();
        }
    }
}
