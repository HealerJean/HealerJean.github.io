package com.healerjean.proj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.google.common.collect.Lists;
import com.healerjean.proj.dto.excel.fill.FillData;
import com.healerjean.proj.service.FillExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2021/12/17  8:48 下午.
 * @description
 */
@Service
@Slf4j
public class FillExcelServiceImpl implements FillExcelService {

    private static final String PATH = "/Users/healerjean/Desktop/data/fill/";
    private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";

    private static final String FILL_SIMPLE_EXCEL_FILE = PATH + "simple.xlsx";
    private static final String FILL_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    private static final String FILL_LIST_EXCEL_FILE = PATH + "list.xlsx";
    private static final String FILL_LIST_EXCEL_RESULT_FILE = PATH + "listFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    private static final String FILL_COMPLEX_EXCEL_FILE = PATH + "complex.xlsx";
    private static final String FILL_COMPLEX_EXCEL_RESULT_FILE = PATH + "complexFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    private static final String FILL_COMPLEXFILLWITHTABLE_EXCEL_FILE = PATH + "complexFillWithTable.xlsx";
    private static final String FILL_COMPLEXFILLWITHTABLE_EXCEL_RESULT_FILE = PATH + "complexFillWithTableFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    private static final String FILL_HORIZONTAL_EXCEL_FILE = PATH + "horizontal.xlsx";
    private static final String FILL_HORIZONTAL_EXCEL_RESULT_FILE = PATH + "horizontalFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

    private static final String FILL_COMPOSITE_EXCEL_FILE = PATH + "composite.xlsx";
    private static final String FILL_COMPOSITE_EXCEL_RESULT_FILE = PATH + "compositeFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";





    /**
     * 1、最简单的填充（所有字段都填充）
     * 注意: 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
     */
    @Override
    public void simpleFill() {
        // 方案1 根据对象填充
        String newFileName = FILL_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        FillData fillData = new FillData();
        fillData.setName("张三");
        fillData.setNumber(5.2);
        EasyExcel.write(newFileName).withTemplate(FILL_SIMPLE_EXCEL_FILE).sheet().doFill(fillData);

        // 方案2 根据Map填充
        newFileName = FILL_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("name", "张三");
        map.put("number", 5.2);
        EasyExcel.write(newFileName).withTemplate(FILL_SIMPLE_EXCEL_FILE).sheet().doFill(map);
    }

    /**
     * 2、填充列表
     * 注意： 填充list 的时候还要注意 模板中{.} 多了个点 表示list
     */
    @Override
    public void listFill() {
        // 方案1 一下子全部放到内存里面 并填充
        String newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).sheet().doFill(data());

        // 方案2 分多次 填充 会使用文件缓存（省内存）
        newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).sheet().doFill(() -> data());

        // 方案3 分多次 填充 会使用文件缓存（省内存）
        newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data(), writeSheet);
        excelWriter.fill(data(), writeSheet);
        // 千万别忘记关闭流
        excelWriter.finish();
    }


    /**
     * 3、复杂的填充
     *  注意： 1.入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。
     *        2.默认 是false，会直接使用下一行，如果没有则创建。
     *        3.forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
     *        4.简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
     *        5.如果数据量大 list不是最后一行 参照下一个用例方法
     */
    @Override
    public void complexFill() {
        String newFileName = FILL_COMPLEX_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPLEX_EXCEL_FILE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();

        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(data(), fillConfig, writeSheet);
        excelWriter.fill(data(), fillConfig, writeSheet);

        Map<String, Object> map = MapUtils.newHashMap();
        map.put("date", "2019年10月9日13:28:28");
        map.put("total", 1000);
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }


    /**
     * 4、数据量大的复杂填充
     * 说明：这里模板 删除了list以后的数据，也就是统计的这一行，所以和3中的模板本身就不一样，也不知道解决了个啥
     * 作者说明：总体上写法比较复杂 但是也没有想到好的版本 异步的去写入excel 不支持行的删除和移动，也不支持备注这种的写入，所以也排除了可以
     *          新建一个 然后一点点复制过来的方案，最后导致list需要新增行的时候，后面的列的数据没法后移，后续会继续想想解决方案
     */
    @Override
    public void complexFillWithTable() {
        String newFileName = FILL_COMPLEXFILLWITHTABLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPLEXFILLWITHTABLE_EXCEL_FILE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data(), writeSheet);
        excelWriter.fill(data(), writeSheet);
        // 写入list之前的数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2019年10月9日13:28:28");
        excelWriter.fill(map, writeSheet);

        List<List<String>> totalListList = ListUtils.newArrayList();
        List<String> totalList = ListUtils.newArrayList();
        totalListList.add(totalList);
        totalList.add(null);
        totalList.add(null);
        totalList.add(null);
        totalList.add("统计:1000");
        // 这里是write 别和fill 搞错了
        excelWriter.write(totalListList, writeSheet);
        excelWriter.finish();
    }


    /**
     * 5、横向填充
     */
    @Override
    public void horizontalFill() {
        String newFileName = FILL_HORIZONTAL_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_HORIZONTAL_EXCEL_FILE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
        excelWriter.fill(data(), fillConfig, writeSheet);
        excelWriter.fill(data(), fillConfig, writeSheet);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2019年10月9日13:28:28");
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }


    /**
     * 6、多列表组合填充
     * 说明： {} 代表普通变量 {.} 代表是list的变量 {前缀.} 前缀可以区分不同的list
     */
    @Override
    public void compositeFill() {
        String newFileName = FILL_COMPOSITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPOSITE_EXCEL_FILE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
        excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("data2", data()), writeSheet);
        excelWriter.fill(new FillWrapper("data2", data()), writeSheet);
        excelWriter.fill(new FillWrapper("data3", data()), writeSheet);
        excelWriter.fill(new FillWrapper("data3", data()), writeSheet);

        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("date", "2019年10月9日13:28:28");
        map.put("date", new Date());
        excelWriter.fill(map, writeSheet);
        // 别忘记关闭流
        excelWriter.finish();
    }

    private List<FillData> data() {
        List<FillData> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            FillData fillData = new FillData();
            list.add(fillData);
            fillData.setName("张三");
            fillData.setNumber(5.2);
            fillData.setDate(new Date());
        }
        return list;
    }


}
