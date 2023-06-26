package com.healerjean.proj.utils.file;

import com.healerjean.proj.dto.csv.DemoExcel;

/**
 * ExcelEnums
 *
 * @author HealerJean
 * @date 2023-06-21 02:06:04
 */
public class ExcelEnums {

    /**
     * Excel写入枚举
     */
    public enum Excel {
        /**
         * Excel写入枚举
         */
        DEMO_EXCEL("DemoExcel", DemoExcel.class) {
            @Override
            public String[] header() {
                // 顺序不能轻易变动
                return new String[]{"名称"};
            }

            @Override
            public String[] column() {
                return new String[]{"name"};
            }
        },
        ;

        /**
         * ExcelWriter
         *
         * @param info info
         * @param dataClass dataClass
         */
        Excel(String info, Class dataClass) {
            this.info = info;
            this.dataClass = dataClass;
        }
        /**
         * getInfo
         */
        public String getInfo() {

            return info;
        }

        /**
         * getDataClass
         */
        public Class getDataClass() {
            return dataClass;
        }

        /**
         * 说明
         */
        private final String info;

        /**
         * 导出数据对应Bean
         */
        private final Class dataClass;

        /**
         * 导出表头名
         * 对应写入的展示名
         *
         * @return 表头名
         */
        public abstract String[] header();

        /**
         * 导出列
         * 对应写入bean字段名称
         *
         * @return 列
         */
        public abstract String[] column();
    }

}
