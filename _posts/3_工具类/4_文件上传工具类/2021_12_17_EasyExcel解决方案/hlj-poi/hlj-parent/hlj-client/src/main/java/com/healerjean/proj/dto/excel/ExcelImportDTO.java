package com.healerjean.proj.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ExcelImportDTO
 * @author zhangyujin
 * @date 2023/6/25$  10:30$
 */
@Accessors(chain = true)
@Data
public class ExcelImportDTO implements Serializable {


    /**
     * successCount
     */
    private Integer successCount;

    /**
     * failCount
     */
    private Integer failCount;

    /**
     * time
     */
    private Long time;
}
