package com.healerjean.proj.dto.csv;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

/**
 * @author zhangyujin
 * @date 2022/3/28  17:55.
 * @description
 */
@Data
public class CsvDemoDTO {

    @CsvBindByPosition(position = 0)
    private String name;

    // @CsvBindByPosition(position = 1)
    // private Integer age;


}
