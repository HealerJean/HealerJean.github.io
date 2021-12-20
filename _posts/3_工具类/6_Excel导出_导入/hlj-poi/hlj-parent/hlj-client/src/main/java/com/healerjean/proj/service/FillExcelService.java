package com.healerjean.proj.service;

/**
 * @author zhangyujin
 * @date 2021/12/17  8:48 下午.
 * @description
 */
public interface FillExcelService {

    /**
     * 1、最简单的填充
     */
    void simpleFill();

    /**
     * 2、填充列表
     */
    void listFill();


    /**
     * 3、复杂的填充
     */
    void complexFill();

    /**
     * 4、数据量大的复杂填充
     */
    void complexFillWithTable();

    /**
     * 5、横向填充
     */
    void horizontalFill();

    /**
     * 6、多列表组合填充
     */
    void compositeFill();
}
