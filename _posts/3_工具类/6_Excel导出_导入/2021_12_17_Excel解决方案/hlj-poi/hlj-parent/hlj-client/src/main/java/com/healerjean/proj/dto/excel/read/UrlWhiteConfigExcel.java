package com.healerjean.proj.dto.excel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyujin
 * @date 2022/5/19  21:03.
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlWhiteConfigExcel {

    /**
     * 商家Id
     */
    @ExcelProperty(index = 0)
    @CsvBindByPosition(position = 0)
    private String venderId;

}
