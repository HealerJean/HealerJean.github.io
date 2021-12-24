package com.healerjean.proj.dto.excel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础数据类
 **/
@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(10)
@HeadRowHeight(20)
@ColumnWidth(25)
public class WidthAndHeightData {

    @ExcelProperty("字符串标题")
    private String string;

    @ExcelProperty("日期标题")
    private Date date;

    /** 宽度为50  */
    @ColumnWidth(50)
    @ExcelProperty("数字标题")
    private Double doubleData;

}
