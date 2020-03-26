package hlj.wordtopdf.proj.wordToPdf.xdocreport;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ELMode;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.builder.StyleBuilder;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.util.BytePictureUtils;
import hlj.wordtopdf.dto.Person;
import hlj.wordtopdf.dto.TableData;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName MainTest
 * @date 2019/12/5  18:51.
 * @Description
 */
public class MainTest {


    @Test
    public void text() throws Exception {

        String inDocxFilePath = "D:/pdf/text.docx";
        String outDocxFilePath = "D:/pdf/out_text.docx";

        Map<String, Object> map = new HashMap<>();
        // 1.1、普通字符
        map.put("word", "helloWord");

        //1.2、配置格式
        Style style = StyleBuilder.newBuilder().buildColor("00FF00").buildBold().build();
        map.put("author", new TextRenderData("HealerJean", style));

        //1.3、超链接
        map.put("website", new HyperLinkTextRenderData("website.", "http://www.deepoove.com"));


        // 2、对象
        Person person = new Person("HealerJean", "25", "男");
        map.put("person", person);


        //制作模板
        //文件路径获取template
        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath).render(map);

        //如果希望更改语言前后缀为 ${var}
        // Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
        // XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);


        //开始生成新的word
        template.writeToFile(outDocxFilePath);
        template.close();
    }


    @Test
    public void image() throws Exception {
        String inDocxFilePath = "D:/pdf/image.docx";
        String outDocxFilePath = "D:/pdf/out_image.docx";
        String imagePath = "D:/pdf/image.png";
        Map<String, Object> map = new HashMap<>();

        // 本地图片
        map.put("localPicture", new PictureRenderData(120, 120, imagePath));

        // 图片流文件
        InputStream inputStream = new FileInputStream(imagePath);
        map.put("localBytePicture", new PictureRenderData(100, 120, ".png", inputStream));

        // 网络图片
        map.put("urlPicture", new PictureRenderData(100, 100, ".png", BytePictureUtils.getUrlBufferedImage("https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg")));

        // java 图片
        BufferedImage bufferImage = ImageIO.read(new FileInputStream(imagePath));
        map.put("bufferImagePicture", new PictureRenderData(100, 120, ".png", bufferImage));


        //如果希望更改语言前后缀为 ${var}
        Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);
        template.writeToFile(outDocxFilePath);
        template.close();
    }


    @Test
    public void table() throws Exception {
        String inDocxFilePath = "D:/pdf/table.docx";
        String outDocxFilePath = "D:/pdf/out_table.docx";
        Map<String, Object> map = new HashMap<>();

        TableData table1 = new TableData("11", "12", "13");
        TableData table2 = new TableData("21", "22", "23");
        List<TableData> table = new ArrayList<>();
        table.add(table1);
        table.add(table2);

        // RowRenderData header = RowRenderData.build("one", "two", "three");
        //使用样式
        Style style = StyleBuilder.newBuilder().buildColor("00FF00").buildBold().build();
        RowRenderData header = RowRenderData.build(new TextRenderData("one", style), new TextRenderData("two"), new TextRenderData("three"));

        List<RowRenderData> tableBody = new ArrayList<>();
        for (TableData item : table) {
            RowRenderData row = RowRenderData.build(item.getOne(), item.getTwo(), item.getThree());
            tableBody.add(row);
        }
        map.put("table", new MiniTableRenderData(header, tableBody));

        Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);
        template.writeToFile(outDocxFilePath);
        template.close();


    }


    @Test
    public void list() throws Exception {
        String inDocxFilePath = "D:/pdf/list.docx";
        String outDocxFilePath = "D:/pdf/out_list.docx";
        Map<String, Object> map = new HashMap<>();

        map.put("unorderlist", new NumbericRenderData(new ArrayList<TextRenderData>() {{
            add(new TextRenderData("one"));
            add(new TextRenderData("two"));
            add(new TextRenderData("three"));
        }}));
        map.put("orderlist", new NumbericRenderData(NumbericRenderData.FMT_DECIMAL, new ArrayList<TextRenderData>() {{
            add(new TextRenderData("one"));
            add(new TextRenderData("two"));
            add(new TextRenderData("three"));
        }}));


        //如果希望更改语言前后缀为 ${var}
        Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);
        template.writeToFile(outDocxFilePath);
        template.close();
    }


    @Test
    public void rule() throws Exception {

        String inDocxFilePath = "D:/pdf/rule.docx";
        String outDocxFilePath = "D:/pdf/out_rule.docx";

        Map<String, Object> map = new HashMap<>();
        // 1.1、普通字符
        map.put("rule", "helloWord");
        map.put("price", 1);


        //制作模板
        Configure.ConfigureBuilder builder = Configure.newBuilder();
        builder.buildGramer("${", "}");

        // 一个模板标签表达式的结果无法被计算的时候，可以通过ElMode来配置行为：
        // 默认行为，EL静默模式，表达式计算错误的情况下结果置为null
        builder.setElMode(ELMode.POI_TL_STANDARD_MODE);
        // 严格EL模式，表达式计算错误的情况下抛出异常，这种情况下要求表达式必须可被计算
        // builder.setElMode(ELMode.POI_TL_STICT_MODE);

        // 模板标签表达式找不到对应数据、计算结果为null或者数据不合法的时候，可以配置模板标签的行为：

        // 默认行为，静默删除文档中该标签
        builder.setValidErrorHandler(new AbstractRenderPolicy.ClearHandler());
        // 什么都不做，文档中保留该标签
        // builder.setValidErrorHandler(new AbstractRenderPolicy.DiscardHandler());
        // 中断执行，抛出异常
        // builder.setValidErrorHandler(new AbstractRenderPolicy.AbortHandler());

        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder.build()).render(map);


        //开始生成新的word
        template.writeToFile(outDocxFilePath);
        template.close();

    }





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
