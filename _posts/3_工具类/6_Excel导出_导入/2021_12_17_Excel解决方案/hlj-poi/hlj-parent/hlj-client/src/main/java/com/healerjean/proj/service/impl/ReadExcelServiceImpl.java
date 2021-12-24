package com.healerjean.proj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.healerjean.proj.dto.excel.read.DemoData;
import com.healerjean.proj.dto.excel.read.ConverterData;
import com.healerjean.proj.dto.excel.read.DemoExtraData;
import com.healerjean.proj.dto.excel.read.ExceptionDemoData;
import com.healerjean.proj.dto.excel.read.IndexOrNameData;
import com.healerjean.proj.listener.*;
import com.healerjean.proj.service.ReadExcelService;
import com.healerjean.proj.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2021/12/17  5:18 下午.
 * @description
 */
@Slf4j
@Service
public class ReadExcelServiceImpl implements ReadExcelService {

    private static final String PATH = "/Users/healerjean/Desktop/data/";
    private static final String DEMO_EXCEL_FILE = PATH + "demo.xlsx";
    private static final String EXTRA_EXCEL_FILE = PATH + "extra.xlsx";

    /**
     * 1、最简单的读
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * 3. 直接读即可
     */
    public void simpleRead(String filePath) {
        filePath = StringUtils.isBlank(filePath) ? DEMO_EXCEL_FILE : filePath;
        // 1、默认读取一地个sheet
        EasyExcel.read(filePath, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    /**
     * 2、指定列的下标或者列名
     */
    @Override
    public void indexOrNameRead(String filePath) {
        filePath = StringUtils.isBlank(filePath) ? DEMO_EXCEL_FILE : filePath;
        // 这里默认读取第一个sheet
        EasyExcel.read(filePath, IndexOrNameData.class, new IndexOrNameDataListener()).sheet().doRead();
    }


    /**
     * 3、读多个或者全部sheet
     */
    @Override
    public void repeatedRead() {
        // 3.1、读取全部sheet
        // 注意： DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, new DemoDataListener()).doReadAll();

        // 3.2、读取部分sheet
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(DEMO_EXCEL_FILE).build();
            ReadSheet readSheet1 =   EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
            ReadSheet readSheet2 =   EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
            excelReader.read(readSheet1, readSheet2);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }


    /**
     * 4、日期、数字、自定义格式转化
     */
    public void converterRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(DEMO_EXCEL_FILE, ConverterData.class, new ConverterDataListener()).sheet().doRead();
    }


    /**
     * 5、多行头 (默认1行，这里指定有2行标题)
     */
    public void complexHeaderRead() {
        EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, new DemoDataListener()).sheet().headRowNumber(2).doRead();
    }

    /**
     * 6、读取表头数据
     */
    @Override
    public void headerRead() {
        EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, new DemoHeadDataListener()).sheet().doRead();
    }


    /**
     *  7、额外信息（批注、超链接、合并单元格信息读取）
     */
    @Override
    public void extraRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(EXTRA_EXCEL_FILE, DemoExtraData.class, new DemoExtraListener())
                // 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
    }


    /**
     * 8、数据转化等异常处理
     */
    @Override
    public void exceptionRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(DEMO_EXCEL_FILE, ExceptionDemoData.class, new DemoExceptionListener()).sheet().doRead();
    }

    /**
     * 9、同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    @Override
    public void synchronousRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<DemoData> list = EasyExcel.read(DEMO_EXCEL_FILE).head(DemoData.class).sheet().doReadSync();
        for (DemoData data : list) {
            log.info("读取到数据:{}", JsonUtils.toJsonString(data));
        }

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        List<Map<Integer, String>> listMap = EasyExcel.read(DEMO_EXCEL_FILE).sheet().doReadSync();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            log.info("读取到数据:{}", JsonUtils.toJsonString(data));
        }
    }

    /**
     * 10、不创建对象的读
     */
    @Override
    public void noModelRead() {
        EasyExcel.read(DEMO_EXCEL_FILE, new NoModelDataListener()).sheet().doRead();
    }
}
