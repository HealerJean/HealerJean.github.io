package hlj.wordtopdf;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.builder.StyleBuilder;
import com.deepoove.poi.data.style.Style;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName PoiTlUtils
 * @date 2019/12/5  17:19.
 * @Description <dependency>
 * <groupId>com.deepoove</groupId>
 * <artifactId>poi-tl</artifactId>
 * <version>1.6.0</version>
 * </dependency>
 */
public class PoiTlUtils {


    public static void main(String[] args) throws Exception {

        Map<String, Object> map = new HashMap<>();
        // 1、普通字符
        map.put("word", "helloWord");

        //带有样式的
        Style style = StyleBuilder.newBuilder().buildColor("00FF00").buildBold().build();
        map.put("author", new TextRenderData("HealerJean", style));
        //超链接
        map.put("link", new HyperLinkTextRenderData("website.", "http://www.deepoove.com"));


        //  2、对象
        Person person = new Person("HealerJean", "25", "男");
        map.put("person", person);

        // 3、 图片
        map.put("img", new PictureRenderData(100, 120, ".png", new FileInputStream("D:/pdf/img.png")));


        // 3、List表格
        TableData table1 = new TableData("11", "12", "13");
        TableData table2 = new TableData("21", "22", "23");
        List<TableData> table = new ArrayList<>();
        table.add(table1);
        table.add(table2);
        map.put("table", table);

        RowRenderData header = RowRenderData.build("one", "two", "three");
        //使用样式
        // RowRenderData header = RowRenderData.build(new TextRenderData("one"), new TextRenderData("two"), new TextRenderData("three"));

        List<RowRenderData> tableBody = new ArrayList<>();
        table.stream().forEach(item -> {
            RowRenderData row = RowRenderData.build(item.getOne(), item.getTwo(), item.getThree());
            tableBody.add(row);
        });
        map.put("table", new MiniTableRenderData(header, tableBody));

        // XWPFTemplate template = XWPFTemplate.compile("D:/pdf/template_poi_tl.docx").render(map);
        Configure.ConfigureBuilder builder = Configure.newBuilder();
        builder.buildGramer("${", "}");
        FileInputStream fileInputStream = new FileInputStream("D:/pdf/template_poi_tl.docx");
        XWPFTemplate template = XWPFTemplate.compile(fileInputStream, builder.build()).render(map);

        String outDocxFilePath = "D:/pdf/outDocxFile.docx";
        File outDocxFile = new File(outDocxFilePath);
        FileOutputStream outputStream = new FileOutputStream(outDocxFile);
        // 输出到任何流
        template.write(outputStream);

        // 便捷的输出到文件
        outputStream.flush();
        outputStream.close();
        template.close();
    }



}
