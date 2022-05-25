package com.healerjean.proj.utils.excel.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
public final class ExcelWriterUtils {
    /**
     * CSV文件后缀
     */
    private static final String CSV_FILE_SUFFIX = ".csv";
    /**
     * 列顺序
     */
    public final String[] columnMapping;
    /**
     * 表头顺序
     */
    public final String[] headerMapping;

    /**
     * 构造函数
     *
     * @param columnMapping 列顺序
     * @param headerMapping 表头顺序
     */
    private ExcelWriterUtils(String[] columnMapping, String[] headerMapping) {
        this.columnMapping = columnMapping;
        this.headerMapping = headerMapping;
    }

    /**
     * 获取实例
     *
     * @param columnMapping 列顺序
     * @param headerMapping 表头顺序
     * @return 实例
     */
    public static ExcelWriterUtils getInstance(String[] columnMapping, String[] headerMapping) {
        return new ExcelWriterUtils(columnMapping, headerMapping);
    }

    /**
     * 写入CSV
     *
     * @param dataList 数据列表
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return 生成后的路径
     */
    public <E> String writeCsv(List<E> dataList, String fileName, String filePath, Class<E> type) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        // 服务器绝对路径
        File localDir = new File(filePath);
        if (!localDir.exists()) {
            boolean result = localDir.mkdirs();
            if (!result) {
                log.error("writeCsv 创建目录失败:{}", filePath);
                throw new RuntimeException("writeCsv 创建目录失败");
            }
        }
        CSVWriter csvWriter;
        // 写入文件的绝对路径
        String writeFilePath = filePath + fileName;
        if (!fileName.contains(CSV_FILE_SUFFIX)) {
            writeFilePath += CSV_FILE_SUFFIX;
        }
        try (Writer writer = new FileWriter(writeFilePath)) {
            // 手动增加BOM标识
            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            // 映射策略
            ColumnPositionMappingStrategy<E> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(type);
            strategy.setColumnMapping(columnMapping);
            csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            // 写表头
            csvWriter.writeNext(headerMapping);
            StatefulBeanToCsv<E> beanToCsv = new StatefulBeanToCsvBuilder<E>(writer).
                    withMappingStrategy(strategy).
                    withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).
                    withSeparator(CSVWriter.DEFAULT_SEPARATOR).
                    withEscapechar('\\').build();
            beanToCsv.write(dataList);
            csvWriter.close();
            return writeFilePath;
        } catch (IOException e) {
            log.error("CsvWriterUtils writeCsv IOException", e);
        } catch (CsvRequiredFieldEmptyException e) {
            log.error("CsvWriterUtils writeCsv CsvRequiredFieldEmptyException", e);
        } catch (CsvDataTypeMismatchException e) {
            log.error("CsvWriterUtils writeCsv CsvDataTypeMismatchException", e);
        }
        return null;
    }
}
