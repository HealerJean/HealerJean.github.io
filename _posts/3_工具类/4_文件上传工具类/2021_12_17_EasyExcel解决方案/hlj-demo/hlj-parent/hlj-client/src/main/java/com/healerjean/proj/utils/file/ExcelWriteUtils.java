package com.healerjean.proj.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.healerjean.proj.enums.ExcelEnum;
import com.healerjean.proj.utils.date.DateUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongFunction;

/**
 * ExcelWriteResult
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class ExcelWriteUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7324987042050706041L;


    /**
     * 写入总数量
     */
    private AtomicLong count;

    /**
     * 写入单元格
     */
    private WriteSheet writeSheet;

    /**
     * 写
     */
    private ExcelWriter excelWriter;

    /**
     * 写文件路径
     */
    private String filePath;


    /**
     * ExcelWriteHolderInstance
     *
     * @param excelObjEnum excelObjEnum
     * @return {@link ExcelWriteUtils}
     */
    public static ExcelWriteUtils instance(ExcelEnum.ExcelObjEnum excelObjEnum) {
        String filePath = ExcelUtils.DEFAULT_LOCAL_PATH + DateUtils.toDateString(LocalDateTime.now(), DateUtils.YYYYMMDD) + "/" + excelObjEnum.name().toLowerCase() + "/";
        String fileName = System.currentTimeMillis() + ".csv";
        String file = filePath + fileName ;
        // FileUtils.createDir(file, true);
        return new ExcelWriteUtils()
                .setCount(new AtomicLong(0))
                .setExcelWriter(EasyExcel.write(file, excelObjEnum.getClazz()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).autoCloseStream(false).build())
                .setWriteSheet(EasyExcel.writerSheet(1, "Sheet1").build())
                .setFilePath(file);
    }

    /**
     * close
     */
    public void close() {
        if (excelWriter != null) {
            excelWriter.finish();
        }

        // todo
        //清除文件
        // FileUtils.cleanFile(new File(filePath));
    }

    /**
     * 写数据
     *
     * @param list list
     */
    public <T> ExcelWriteUtils write(List<T> list) {
        writeSheet.setSheetNo((int) (count.addAndGet(list.size()) / ExcelUtils.EXCEL_SHEET_ROW_MAX_SIZE + 1));
        writeSheet.setSheetName("Sheet" + writeSheet.getSheetNo());
        excelWriter.write(list, writeSheet);
        return this;
    }

    /**
     * write
     *
     * @param pageFunction pageFunction
     * @return {@link ExcelWriteUtils}
     */
     public <T> ExcelWriteUtils pageWrite(LongFunction<List<T>> pageFunction){
         long pageNo = 1;
         while (true){
             List<T> rpcList =  pageFunction.apply(pageNo);
             if (CollectionUtils.isEmpty(rpcList)){
                 break;
             }
             this.write(rpcList);
             pageNo++;
         }
         close();
         return this;
     }


}
