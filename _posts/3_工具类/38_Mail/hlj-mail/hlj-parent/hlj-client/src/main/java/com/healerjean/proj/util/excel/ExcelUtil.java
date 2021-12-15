package com.healerjean.proj.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/12/15  5:56 下午.
 * @description
 */

public class ExcelUtil {
    public static final String XLS = ".xls";
    public static final String XLSX = ".xlsx";

    public ExcelUtil() {
    }

    public static Workbook getWeebWork(String filename) throws IOException {
        Workbook workbook = null;
        if (null != filename) {
            String fileType = filename.substring(filename.lastIndexOf("."));
            FileInputStream fileStream = new FileInputStream(new File(filename));
            if (".xls".equals(fileType.trim().toLowerCase())) {
                workbook = new HSSFWorkbook(fileStream);
            } else if (".xlsx".equals(fileType.trim().toLowerCase())) {
                workbook = new XSSFWorkbook(fileStream);
            }
        }

        return (Workbook)workbook;
    }

    public static void writeExcel(OutputStream os, List<?> headList, List<List<Object>> dataList) throws IOException {
        Workbook wb = new SXSSFWorkbook(1000);
        CellStyle style = getCellStyle(wb);
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);

        int i;
        for(i = 0; i < headList.size(); ++i) {
            Cell headCell = row.createCell(i);
            headCell.setCellType(CellType.STRING);
            headCell.setCellStyle(style);
            headCell.setCellValue(String.valueOf(headList.get(i)));
        }

        for(i = 0; i < dataList.size(); ++i) {
            Row rowdata = sheet.createRow(i + 1);
            List<Object> mapdata = (List)dataList.get(i);
            int j = 0;

            for(Iterator var11 = mapdata.iterator(); var11.hasNext(); ++j) {
                Object tt = var11.next();
                Cell celldata = rowdata.createCell(j);
                if (tt == null) {
                    tt = "";
                }

                if (tt != null && tt instanceof BigDecimal) {
                    celldata.setCellType(CellType.NUMERIC);
                    celldata.setCellValue(((BigDecimal)tt).doubleValue());
                } else {
                    celldata.setCellType(CellType.STRING);
                    celldata.setCellValue(String.valueOf(tt));
                }
            }
        }

        wb.write(os);
        os.flush();
    }

    public static CellStyle getCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIME.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        return style;
    }
}
