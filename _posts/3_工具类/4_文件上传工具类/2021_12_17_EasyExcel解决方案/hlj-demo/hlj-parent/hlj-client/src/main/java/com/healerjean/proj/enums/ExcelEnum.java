package com.healerjean.proj.enums;

import com.healerjean.proj.data.excel.UserDemoExportExcel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ExcelEnum
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public interface ExcelEnum {

    /**
     * Excel对象
     */
    @Getter
    @AllArgsConstructor
    enum ExcelObjEnum {
        /**
         * USER_DEMO_EXCEL
         */
        EXPORT_EXCEL_USER_DEMO(UserDemoExportExcel.class),
        ;
        private final Class<?> clazz;


    }
}
