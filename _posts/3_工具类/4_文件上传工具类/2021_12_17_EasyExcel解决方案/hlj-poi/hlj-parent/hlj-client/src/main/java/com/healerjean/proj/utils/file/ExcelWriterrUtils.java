package com.healerjean.proj.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ExcelWriterrUtils
 *
 * @author zhangyujin
 * @date 2023/6/21$  15:38$
 */
@Slf4j
public final class ExcelWriterrUtils {

    /**
     * 默认文件路径
     */
    private static final String DEFAULT_LOCAL_PATH = "/Users/healerjean/Desktop/logs/";

    /**
     * 写入Excel
     *
     * @param dataList 数据列表
     * @param fileName 文件名称
     * @param excel    写入文件类型
     * @return 生成后的路径
     */
    public static <E> String writerExcel(List<E> dataList, String fileName, ExcelEnums.Excel excel) {
        ExcelWriter excelWriter = null;
        try {
            if (null == dataList) {
                dataList = new ArrayList<>();
            }
            String date = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
            String baseLocalPath = DEFAULT_LOCAL_PATH + excel.name().toLowerCase() + "/" + date + "/";

            // 创建目录
            FileUtils.createDir(baseLocalPath, Boolean.FALSE);
            String writeFilePath = baseLocalPath + System.currentTimeMillis() + "_" + fileName;
            excelWriter = EasyExcel.write(writeFilePath, excel.getDataClass()).build();
            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 设置表头居中对齐
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // 颜色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headWriteCellStyle.setWrapped(true);
            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置内容靠中对齐
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            // 设置文本类型
            DataFormatData dataFormatData = new DataFormatData();
            dataFormatData.setIndex((short) 49);
            contentWriteCellStyle.setDataFormatData(dataFormatData);
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            WriteSheet writeSheet = EasyExcel.writerSheet().registerWriteHandler(horizontalCellStyleStrategy).
                    registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                        @Override
                        protected void setColumnWidth(CellWriteHandlerContext context) {
                            Sheet sheet = context.getWriteSheetHolder().getSheet();
                            Cell cell = context.getCell();
                            sheet.setColumnWidth(cell.getColumnIndex(), 5000);
                        }
                    }).
                    includeColumnFiledNames(Arrays.asList(excel.column())).build();
            excelWriter.write(dataList, writeSheet);
            return writeFilePath;
        } catch (Exception e) {
            log.error("ExcelUtils writerExcel exception", e);
        } finally {
            //关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return null;
    }


}
