package com.healerjean.proj.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangyujin
 * @date 2021/12/20  8:03 下午.
 * @description
 */

public interface DemoExcelService {

    /**
     * 数据量少的(20W以内吧)：一个SHEET一次查询导出
     */
    void exportBigDataExcel(HttpServletResponse response);
}





