package com.healerjean.proj.dto.csv;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * CsvDemoExcel
 * @author zhangyujin
 * @date 2022/3/28  17:55.
 */
@Data
public class DemoExcel {

    /**
     * name
     */
    @ExcelProperty(value = "名称")
    private String name;


}
