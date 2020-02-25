package com.healerjean.proj.util.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName POIExcelHelper
 * @Date 2020/2/25  19:37.
 * @Description
 */
public class POIExcelHelper {

    // 总行数
    private int totalRows = 0;
    // 总列数
    private int totalCells = 0;

    public POIExcelHelper() {
    }

    /**
     * 根据流读取Excel文件
     *
     * @param inputStream 流
     * @param isExcel2003 是否2003版本
     * @return
     */
    public List<ArrayList<String>> read(InputStream inputStream, boolean isExcel2003) {
        List<ArrayList<String>> dataLst = null;
        try {
            // 根据版本选择创建Workbook的方式
            Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
            dataLst = read(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataLst;
    }

    //得到总行数
    private int getTotalRows() {
        return totalRows;
    }

    //得到总列数
    private int getTotalCells() {
        return totalCells;
    }

    // 读取数据
    private List<ArrayList<String>> read(Workbook wb) {
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        // 循环Excel的行
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

            ArrayList<String> rowLst = new ArrayList<String>();
            // 循环Excel的列
            for (short c = 0; c < this.getTotalCells(); c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (cell == null) {
                    rowLst.add(cellValue);
                    continue;
                }


                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = dateFormat.format(cell.getDateCellValue());
                    } else {
                        cellValue = getRightStr(cell.getNumericCellValue() + "");
                    }
                }
                // 处理字符串型
                else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                    cellValue = cell.getStringCellValue();
                    // 处理布尔型
                } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                    cellValue = cell.getBooleanCellValue() + "";
                    // 其它数据类型
                } else {
                    cellValue = cell.toString() + "";
                }

                rowLst.add(cellValue);
            }
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    // 正确地处理整数后自动加零的情况
    private String getRightStr(String sNum) {
        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        String resultStr = decimalFormat.format(Double.valueOf(sNum));
        if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
            resultStr = resultStr.substring(0, resultStr.indexOf("."));
        } else if (new BigDecimal(sNum).compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return resultStr;
    }


}
