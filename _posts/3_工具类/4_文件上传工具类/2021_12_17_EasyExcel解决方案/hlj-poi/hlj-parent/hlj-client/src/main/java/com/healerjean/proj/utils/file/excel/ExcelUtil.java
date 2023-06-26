package com.healerjean.proj.utils.file.excel;


import com.alibaba.excel.EasyExcel;
import com.healerjean.proj.dto.excel.ExcelImportDTO;
import com.healerjean.proj.listener.GeoPageReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ExcelUtil
 *
 * @author HealerJean
 * @date 2023-06-25 10:06:00
 */
@Slf4j
public class ExcelUtil {

    /**
     * 异步多线程导入数据
     * 采用自定义注入mybatis-plus的SQL注入器，实现真正的BatchInsert，但是需要注意的是项目配置文件需要在jdbc的url后面加上rewriteBatchedStatements=true
     *
     * @param head       Excel导入实体类的class
     * @param file       要导入的Excel文件
     * @param function   数据处理函数，对数据加工
     * @param dbFunction 数据库操作
     * @param <T>        Excel导入实体类   例如DataSwMbqcdssImportExcelVO
     * @param <R>        数据库实体类  例如DataSwMbqcdssDO
     * @return 导入结果
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private <T, R> ExcelImportDTO readExcelAndSaveAsync(Class<T> head, MultipartFile file, Function<T, R> function, Function<List<R>, Integer> dbFunction) throws IOException, ExecutionException, InterruptedException {
        Integer successCount = 0;
        Integer failCount = 0;
        //存储异步线程的执行结果
        Collection<Future<int[]>> futures = new ArrayList<>();

        EasyExcel.read(file.getInputStream(), head, new GeoPageReadListener<T>(dataList -> {
            //转换DO，并设置数据源id
            List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
            //异步批量插入
            futures.add(saveAsyncBatch(list, dbFunction));
        })).sheet().doRead();
        //等待异步线程执行完毕
        for (Future<int[]> future : futures) {
            int[] counts = future.get();
            successCount += counts[0];
            failCount += counts[1];
        }
        log.info("存储成功总数据量：{},存储失败总数据量:{}", successCount, failCount);
        return new ExcelImportDTO()
                .setSuccessCount(successCount)
                .setFailCount(failCount);
    }


    /**
     * 批量插入
     *
     * @param list       要分批处理的数据
     * @param dbFunction 数据库操作的方法
     * @param <T>        数据库实体类
     * @return 返回处理结果
     */
    @Async
    public <T> Future<int[]> saveAsyncBatch(List<T> list, Function<List<T>, Integer> dbFunction) {
        int size = list.size();
        int[] result = new int[2];
        log.info("saveAsyncBatch当前数据分片大小 size:{}", size);
        try {
            if (dbFunction.apply(list) > 0) {
                result[0] = size;
                log.info("{} 分片存储数据成功,数据量：{}", Thread.currentThread().getName(), size);
            } else {
                result[1] = size;
                log.info("{} 分片存储数据失败：{}", Thread.currentThread().getName(), size);
            }
        } catch (Exception e) {
            result[1] = size;
            log.error("{} 分片存储数据出现异常，{}", Thread.currentThread().getName(), e.getMessage());
        }

        return new AsyncResult<>(result);
    }

}