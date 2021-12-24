package com.healerjean.proj.service;

import java.io.IOException;

/**
 * @author zhangyujin
 * @date 2021/12/20  3:30 下午.
 * @description
 */
public interface WriteExcelService {

    /**
     * 1、最简单的写
     */
    void simpleWrite( );

    /**
     * 2、根据参数只导出指定列
     */
    void excludeOrIncludeWrite( );

    /**
     * 3、指定写入的列
     */
    void indexWrite();

    /**
     * 4、复杂头写入
     */
    void complexHeadWrite();

    /**
     * 5、重复多次写入
     */
    void repeatedWrite();

    /**
     * 6、日期、数字或者自定义格式转换
     */
    void converterWrite();

    /**
     * 7、图片导出
     */
    void imageWrite() throws IOException;

    /**
     * 8、根据模板写入
     */
    void templateWrite();

    /**
     * 9、 列宽、行高
     */
    void widthAndHeightWrite();

    /**
     * 10、注解形式自定义样式
     */
    void annotationStyleWrite();

    /**
     * 11、合并单元格
     */
    void mergeWrite();

    /**
     * 12、自动列宽(不太精确)
     */
    void longestMatchColumnWidthWrite();

    /**
     * 13、 动态头，实时生成头写入
     */
    void dynamicHeadWrite();

    /**
     * 14、不创建对象的写
     */
    void noModelWrite();
}
