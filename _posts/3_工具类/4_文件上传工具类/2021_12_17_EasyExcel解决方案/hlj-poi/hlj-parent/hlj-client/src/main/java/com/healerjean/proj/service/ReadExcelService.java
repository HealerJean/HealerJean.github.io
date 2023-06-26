package com.healerjean.proj.service;

/**
 * @author zhangyujin
 * @date 2021/12/17  5:17 下午.
 * @description
 */
public interface ReadExcelService {

    /**
     * 1、简单读取
     */
    void simpleRead(String filePath);

    /**
     * 2、指定列的下标或者列名
     */
    void indexOrNameRead(String filePath);

    /**
     * 3、读多个或者全部sheet
     */
     void repeatedRead();

    /**
     * 4、日期、数字、自定义格式转化
     */
    void converterRead();

    /**
     * 5、多行头 (默认1行，这里指定有2行标题)
     */
    void complexHeaderRead();

    /**
     * 6、读取表头数据
     */
    void headerRead();

    /**
     * 7、额外信息（批注、超链接、合并单元格信息读取）
     */
    void extraRead();


    /**
     * 8、数据转化等异常处理
     */
    void exceptionRead();

    /**
     * 9、同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    void synchronousRead();

    /**
     * 10、不创建对象的读
     */
    void noModelRead();
}
