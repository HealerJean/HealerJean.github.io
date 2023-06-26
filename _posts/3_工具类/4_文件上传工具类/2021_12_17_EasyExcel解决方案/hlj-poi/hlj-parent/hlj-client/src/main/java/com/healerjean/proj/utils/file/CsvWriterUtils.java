package com.healerjean.proj.utils.file;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class CsvWriterUtils {

    /**
     * 默认文件路径
     */
    private static final String DEFAULT_LOCAL_PATH = "/Users/healerjean/Desktop/logs/";


    /**
     * 写入CSV
     *
     * @param dataList   数据列表
     * @param fileName   文件名称
     * @param excel 写入文件类型
     * @return 生成后的路径
     */
    public static <E> String writerCsv(List<E> dataList, String fileName, ExcelEnums.Excel excel) {
        if (null == dataList) {
            dataList = new ArrayList<>();
        }
        // 服务器绝对路径
        String date = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        String baseLocalPath = DEFAULT_LOCAL_PATH + excel.name().toLowerCase() + "/" + date + "/";
        // 创建目录
        FileUtils.createDir(baseLocalPath, Boolean.FALSE);
        log.info("ExcelUtils writeCsv begin,size:{}", dataList.size());
        CSVWriter csvWriter;
        // 写入文件的绝对路径
        String writeFilePath = baseLocalPath + System.currentTimeMillis() + "_" + fileName;
        try (Writer writer = new FileWriter(writeFilePath)) {
            // 手动增加BOM标识
            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            // 映射策略
            ColumnPositionMappingStrategy<E> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(excel.getDataClass());
            strategy.setColumnMapping(excel.column());
            csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            // 写表头
            csvWriter.writeNext(excel.header());
            StatefulBeanToCsv<E> beanToCsv = new StatefulBeanToCsvBuilder<E>(writer).
                    withMappingStrategy(strategy).
                    withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).
                    withSeparator(CSVWriter.DEFAULT_SEPARATOR).
                    withEscapechar('\\').build();
            beanToCsv.write(dataList);
            csvWriter.close();
            log.info("FileUtils writeCsv end,size:{}", dataList.size());
            return writeFilePath;
        } catch (IOException e) {
            log.error("FileUtils writeCsv IOException", e);
        } catch (CsvRequiredFieldEmptyException e) {
            log.error("FileUtils writeCsv CsvRequiredFieldEmptyException", e);
        } catch (CsvDataTypeMismatchException e) {
            log.error("FileUtils writeCsv CsvDataTypeMismatchException", e);
        }
        return null;
    }



}
