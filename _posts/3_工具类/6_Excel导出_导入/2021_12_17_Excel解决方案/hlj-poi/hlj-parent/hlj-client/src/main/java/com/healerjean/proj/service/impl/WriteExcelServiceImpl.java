package com.healerjean.proj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.healerjean.proj.dto.excel.write.*;
import com.healerjean.proj.service.WriteExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author zhangyujin
 * @date 2021/12/20  3:30 下午.
 * @description
 */
@Slf4j
@Service
public class WriteExcelServiceImpl implements WriteExcelService {


    private static final String PATH = "/Users/healerjean/Desktop/data/write/";
    private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";

    private static final String WRITE_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE = PATH + "excludeOrIncludeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_INDEXWRITE_EXCEL_RESULT_FILE = PATH + "indexWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_COMPLEXHEADWRITE_EXCEL_RESULT_FILE = PATH + "complexHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE = PATH + "repeatedWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_CONVERTERWRITE_EXCEL_RESULT_FILE = PATH + "converterWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_IMAGEWRITE_EXCEL_RESULT_FILE = PATH + "imageWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_IMAGEWRITE_IMAGE_FILE = "/Users/healerjean/Desktop/data/write/image/img.jpg";

    private static final String WRITE_DEMO_EXCEL_RESULT_FILE = PATH + "demo.xlsx";
    private static final String WRITE_TEMPLATEWRITE_EXCEL_RESULT_FILE = PATH + "templateWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_WIDTHANDHEIGHTWRITE_EXCEL_RESULT_FILE = PATH + "widthAndHeightWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_ANNOTATIONSTYLEWRITE_EXCEL_RESULT_FILE = PATH + "annotationStyleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_MERGEWRITE_EXCEL_RESULT_FILE = PATH + "mergeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_LONGESTMATCHCOLUMNWIDTHWRITE_EXCEL_RESULT_FILE = PATH + "longestMatchColumnWidthWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_DYNAMICHEADWRITE_EXCEL_RESULT_FILE = PATH + "dynamicHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
    private static final String WRITE_NOMODELWRITE_EXCEL_RESULT_FILE = PATH + "noModelWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";



    /**
     * 1、最简单的写
     */
    @Override
    public void simpleWrite() {
        // 写法1
        String newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, DemoData.class).sheet("模板").doWrite(() -> data());

        // 写法2
        newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, DemoData.class).sheet("模板").doWrite(data());

        // 写法3
        newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 2、根据参数只导出指定列
     * 2.1、根据用户传入字段 假设我们要忽略 date
     * 2.2、根据用户传入字段 假设我们只要导出 date
     */
    @Override
    public void excludeOrIncludeWrite() {
        String newFileName = WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

        // 2.1、根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = Sets.newHashSet("date");
        EasyExcel.write(newFileName, DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("模板").doWrite(data());

        // 2.2、根据用户传入字段 假设我们只要导出 date
        newFileName = WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        Set<String> includeColumnFiledNames = Sets.newHashSet("date");
        EasyExcel.write(newFileName, DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板").doWrite(data());
    }


    /**
     * 3、指定写入的列
     */
    @Override
    public void indexWrite() {
        String newFileName = WRITE_INDEXWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, IndexData.class).sheet("模板").doWrite(data());
    }


    /**
     * 4、复杂头写入
     * 注意：本示例是合并了标题头的单元格
     */
    @Override
    public void complexHeadWrite() {
        String newFileName = WRITE_COMPLEXHEADWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, ComplexHeadData.class).sheet("模板").doWrite(data());
    }


    /**
     * 5、重复多次写入
     */
    @Override
    public void repeatedWrite() {
        // 方法1
        String newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法2 如果写到不同的sheet 同一个对象
        try {
            newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
            // 这里 指定文件
            excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法3 如果写到不同的sheet 不同的对象
        try {
            newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
            // 这里 指定文件
            excelWriter = EasyExcel.write(newFileName).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
                // 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 6、日期、数字或者自定义格式转换
     */
    @Override
    public void converterWrite() {
        String newFileName = WRITE_CONVERTERWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, ConverterData.class).sheet("模板").doWrite(data());
    }


    /**
     * 7、图片导出
     */
    @Override
    public void imageWrite() throws IOException {
        String newFileName = WRITE_IMAGEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        String imagePath = WRITE_IMAGEWRITE_IMAGE_FILE;
        try (InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
            List<ImageDemoData> list = ListUtils.newArrayList();
            ImageDemoData imageDemoData = new ImageDemoData();
            // 放入五种类型的图片 实际使用只要选一种即可
            imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imageDemoData.setFile(new File(imagePath));
            imageDemoData.setString(imagePath);
            imageDemoData.setInputStream(inputStream);
            imageDemoData.setUrl(new URL( "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Farticle-fd.zol-img.com.cn%2Ft_s998x562c5%2Fg5%2FM00%2F0A%2F02%2FChMkJltpVKGIQENcAAKaC93UFtUAAqi5QPdcOwAApoj403.jpg&refer=http%3A%2F%2Farticle-fd.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642583011&t=619dc95b408c5d7bb168e66210be6519"));
            list.add(imageDemoData);
            // 写入数据
            EasyExcel.write(newFileName, ImageDemoData.class).sheet().doWrite(list);
        }
    }


    /**
     * 8、根据模板写入
     * 说明：模板个球，和Fill模板是不一样的
     */
    @Override
    public void templateWrite() {
        String newFileName = WRITE_TEMPLATEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, DemoData.class).withTemplate(WRITE_DEMO_EXCEL_RESULT_FILE).sheet().doWrite(data());
    }

    /**
     * 9、 列宽、行高
     */
    @Override
    public void widthAndHeightWrite() {
        String newFileName = WRITE_WIDTHANDHEIGHTWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, WidthAndHeightData.class).sheet("模板").doWrite(data());
    }


    /**
     * 10、注解形式自定义样式
     */
    @Override
    public void annotationStyleWrite() {
        String newFileName = WRITE_ANNOTATIONSTYLEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, DemoStyleData.class).sheet("模板").doWrite(data());
    }

    /**
     * 11、合并单元格
     */
    @Override
    public void mergeWrite() {
        // 方法1 注解
        String newFileName = WRITE_MERGEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, DemoMergeData.class).sheet("模板").doWrite(data());
    }


    /**
     * 12、自动列宽(不太精确)
      */
    @Override
    public void longestMatchColumnWidthWrite() {
        String  newFileName= WRITE_LONGESTMATCHCOLUMNWIDTHWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName, LongestMatchColumnWidthData.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("模板").doWrite(dataLong());
    }


    /**
     * 13、 动态头，实时生成头写入
     */
    @Override
    public void dynamicHeadWrite() {
        String newFileName = WRITE_DYNAMICHEADWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName) .head(head()).sheet("模板").doWrite(data());
    }


    /**
     * 14、不创建对象的写
     */
    @Override
    public void noModelWrite() {
        String newFileName = WRITE_NOMODELWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        EasyExcel.write(newFileName).head(head()).sheet("模板").doWrite(dataList());
    }


    private List<List<Object>> dataList() {
        List<List<Object>> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            List<Object> data = ListUtils.newArrayList();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            list.add(data);
        }
        return list;
    }

    private List<List<String>> head() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = ListUtils.newArrayList();
        head1.add("日期" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("数字" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }


    private List<LongestMatchColumnWidthData> dataLong() {
        List<LongestMatchColumnWidthData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            LongestMatchColumnWidthData data = new LongestMatchColumnWidthData();
            data.setString("测试很长的字符串测试很长的字符串测试很长的字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(1000000000000.0);
            list.add(data);
        }
        return list;
    }

    private List<DemoData> data() {
        List<DemoData> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    }
