package com.healerjean.proj.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.healerjean.proj.data.excel.UserDemoExportExcel;
import com.healerjean.proj.enums.ExcelEnum;
import com.healerjean.proj.utils.date.DateUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ExcelWriteResult
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class ExcelWriteHolder implements Serializable {

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
     * @return {@link ExcelWriteHolder}
     */
    public static ExcelWriteHolder instance(ExcelEnum.ExcelObjEnum excelObjEnum) {
        String filePath = ExcelUtils.DEFAULT_LOCAL_PATH + DateUtils.toDateString(LocalDateTime.now(), DateUtils.YYYYMMDD) + "/" + excelObjEnum.name().toLowerCase() + "/" + System.currentTimeMillis() + ".xlsx";
        return new ExcelWriteHolder()
                .setCount(new AtomicLong(0))
                .setExcelWriter(EasyExcel.write(filePath, UserDemoExportExcel.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).autoCloseStream(false).build())
                .setWriteSheet(EasyExcel.writerSheet(1, "Sheet1").build())
                .setFilePath(filePath);
    }

    /**
     * close
     */
    public void close() {
        if (excelWriter != null) {
            excelWriter.finish();
        }

        // todo 张宇晋
        //清除文件
        // FileUtils.cleanFile(new File(filePath));
    }

    /**
     * 写数据
     *
     * @param list list
     */
    public ExcelWriteHolder write(List list) {
        writeSheet.setSheetNo((int) (count.addAndGet(list.size()) / ExcelUtils.EXCEL_SHEET_ROW_MAX_SIZE + 1));
        writeSheet.setSheetName("Sheet" + writeSheet.getSheetNo());
        excelWriter.write(list, writeSheet);
        return this;
    }


}
