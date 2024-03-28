package com.healerjean.proj.dto.excel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 复杂头数据.这里最终效果是第一行就一个主标题，第二行分类
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
@EqualsAndHashCode
public class ComplexHeadData {
    @ExcelProperty({"主标题", "字符串标题"})
    private String string;
    @ExcelProperty({"主标题", "日期标题"})
    private Date date;
    @ExcelProperty({"主标题", "数字标题"})
    private Double doubleData;

    @ExcelProperty({"计算类型（1比例 2固定）", "计算类型（1比例 2固定）"})
    private String calculateType;

    @ExcelProperty({"保费（元）比例（%）", "保费（元）比例（%）"})
    private String fee;
}
