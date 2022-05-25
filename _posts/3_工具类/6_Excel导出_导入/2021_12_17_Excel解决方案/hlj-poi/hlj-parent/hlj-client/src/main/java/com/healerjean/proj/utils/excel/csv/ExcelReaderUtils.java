package com.healerjean.proj.utils.excel.csv;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.opencsv.ICSVParser;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelReaderUtils {

    /**
     * 构造函数
     */
    private ExcelReaderUtils() {

    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static ExcelReaderUtils getInstance() {
        return new ExcelReaderUtils();
    }

    public static void readerExcel(InputStream inputStream) {
    }

    /**
     * 读取CSV
     *
     * @param file 文件
     * @param type Class<E>
     * @param <E>  E
     * @return List<E>
     */
    public <E> List<E> readerCsv(File file, Class<E> type) throws FileNotFoundException {
        return readerCsv(file, 1, ICSVParser.DEFAULT_SEPARATOR, type);
    }

    /**
     * 读取CSV
     *
     * @param file      文件
     * @param separator 分隔符
     * @param type      Class<E>
     * @param <E>       E
     * @return List<E>
     */
    public <E> List<E> readerCsv(File file, char separator, Class<E> type) throws FileNotFoundException {
        return readerCsv(file, 1, separator, type);
    }

    /**
     * 读取CSV
     *
     * @param file      文件
     * @param skipLines 跳过前几行读取,默认第一行
     * @param separator 分隔符
     * @param type      Class<E>
     * @param <E>       E
     * @return List<E>
     */
    public <E> List<E> readerCsv(File file, int skipLines, char separator, Class<E> type) throws FileNotFoundException {
        ColumnPositionMappingStrategy<E> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(type);
        CsvToBean<E> build = new CsvToBeanBuilder<E>(new FileReader(file))
                .withSkipLines(skipLines)
                .withMappingStrategy(mappingStrategy)
                .withSeparator(separator).build();
        return build.parse();
    }

    /**
     * 读取CSV
     *
     * @param inputStream 文件
     * @param type Class<E>
     * @param <E>  E
     * @return List<E>
     */
    public static  <E> List<E> readerCsvInputStream(InputStream inputStream, Class<E> type){
        return readerCsvInputStream(inputStream, 1, ICSVParser.DEFAULT_SEPARATOR, type);
    }

    /**
     * 处理Excel数据
     *
     * @param inputStream InputStream
     * @param type        Class<E>
     * @param <E>         E
     * @return List<E>
     */
    public static <E> List<E> readerExcel(InputStream inputStream, Class<E> type) {
        List<E> list = new ArrayList<>();
        EasyExcel.read(inputStream, type, new ReadListener<E>() {
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
     * 读取CSV
     *
     * @param inputStream      文件
     * @param skipLines 跳过前几行读取,默认第一行
     * @param separator 分隔符
     * @param type      Class<E>
     * @param <E>       E
     * @return List<E>
     */
    public static  <E> List<E> readerCsvInputStream(InputStream inputStream, int skipLines, char separator, Class<E> type){
        ColumnPositionMappingStrategy<E> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(type);
        CsvToBean<E> build = new CsvToBeanBuilder<E>(new InputStreamReader(inputStream))
                .withSkipLines(skipLines)
                .withMappingStrategy(mappingStrategy)
                .withSeparator(separator).build();
        return build.parse();
    }

}
