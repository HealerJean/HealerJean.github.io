package com.healerjean.proj.dto.excel.demo;

/**
 * @author zhangyujin
 * @date 2021/12/20  8:04 下午.
 * @description
 */
public interface ExcelConstant {

    /**
     * 每个sheet存储的记录数 100W
     */
    Integer PER_SHEET_ROW_COUNT = 1000000;

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 20W （200000）
     */
    Integer PER_WRITE_ROW_COUNT = 200;

}
