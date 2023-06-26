package com.healerjean.proj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.dto.csv.DemoExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * BigExcelController
 *
 * @author zhangyujin
 * @date 2023/6/21$  20:16$
 */
@RestController("hlj/bigExcel")
@Slf4j
public class BigExcelController {


    /**
     * 导出Excel（并发查询，串行写入）<br>
     * 注意：实际应用中，pageSize和parallelNum参数不应对外开放，此处为方便测试，或pageSize需要设置上限，防止恶意用户传一个很大的pageSize值，而撑满内存。
     * @param response 响应
     * @param parallelNum 并发查询线程数
     * @param pageSize 页大小
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/writeExcelForParallel")
    public void writeExcelForParallel(HttpServletResponse response,
                                      @RequestParam(value = "parallelNum", defaultValue = "3") int parallelNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10000") int pageSize) throws IOException, InterruptedException {
        LambdaQueryWrapper<DemoExcel> queryWrapper = Wrappers.lambdaQuery(DemoExcel.class);

        // 查询总条数，通过设置页大小而获取总页数
        IPage<T> page = page(new Page<>(1, 0), queryWrapper).setSize(pageSize);
        ExcelUtil.writeForParallel(outputStream, getEntityClass(), parallelNum, page.getPages(), pageNo -> page(new Page<>(pageNo, pageSize, page.getTotal(), false), queryWrapper).getRecords());


        demoService.writeExcelForParallel(excelOutput(response, "测试"), parallelNum, pageSize, queryWrapper);
    }


}


