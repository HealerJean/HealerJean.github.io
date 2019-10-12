package hlj.wordtopdf;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName WordToPdfUtils
 * @date 2019/10/12  14:00.
 * @Description
 */
@Data
public class WordToPdfUtils {

    private FieldsMetadata fieldsMetadata;
    private IXDocReport ixDocReport;
    private IContext iContext;


    public WordToPdfUtils(InputStream inputStream) {
        try {
            this.ixDocReport = XDocReportRegistry.getRegistry().loadReport(inputStream, TemplateEngineKind.Freemarker);
            this.iContext = ixDocReport.createContext();
            this.fieldsMetadata = ixDocReport.createFieldsMetadata();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 普通对象
     */
    public void addText(String fieldName, Object text) {
        this.iContext.put(fieldName, text);
    }


    /**
     * 普通对象
     */
    public void addObject(String fieldName, Class<?> c, Object o) {
        try {
            this.fieldsMetadata.load(fieldName, c);
            this.iContext.put(fieldName, o);
        } catch (XDocReportException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * List对象
     */
    public void addList(String fieldName, Class<?> c, List list) {
        try {
            this.fieldsMetadata.load(fieldName, c, true);
            this.iContext.put(fieldName, list);
        } catch (XDocReportException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 图片
     */
    public void addImage(String fieldName, File file) {
        this.fieldsMetadata.addFieldAsImage(fieldName);
        IImageProvider img = new FileImageProvider(file);
        this.iContext.put(fieldName, img);
    }
    public void addImage(String fieldName, File file, float width, float height) {
        this.fieldsMetadata.addFieldAsImage(fieldName);
        IImageProvider img = new FileImageProvider(file, true);
        img.setSize(width, height);
        this.iContext.put(fieldName, img);
    }


    /**
     * 生成Word
     */
    public void createWord(OutputStream outputStream) {
        try {
            this.ixDocReport.process(this.iContext, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 生成Pdf
     */
    public void createPdf(OutputStream outputStream) {
        try {
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            this.ixDocReport.setFieldsMetadata(this.fieldsMetadata);
            this.ixDocReport.convert(this.iContext, options, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public static void main(String[] args) throws IOException, XDocReportException {

        String WORD = "word";
        String PERSON = "person";
        String TABLE = "table";
        String IMG = "img";


        File template = new File("D:/pdf/template.docx");

        WordToPdfUtils wordToPdfUtils = new WordToPdfUtils(new FileInputStream(template));
        Person person = new Person("HealerJean", "25", "男");

        // 1、普通字符
        wordToPdfUtils.addText(WORD, "1");

        // 2、对象
        wordToPdfUtils.addObject(PERSON, Person.class, person);

        // 3、List表格
        Table table1 = new Table("11", "12", "13");
        Table table2 = new Table("21", "22", "23");
        List<Table> table = new ArrayList<>();
        table.add(table1);
        table.add(table2);
        wordToPdfUtils.addList(TABLE, Table.class, table);

        // 4、 图片
        // wordToPdfUtils.addImage(IMG,new File("D:/pdf/img.png"));
        wordToPdfUtils.addImage(IMG, new File("D:/pdf/img.png"), 200f, 100f);

        //生成pdf
        File outputFile = new File("D:/pdf/ok_template.pdf");
        OutputStream outputStream = new FileOutputStream(outputFile);
        wordToPdfUtils.createPdf(outputStream);

        //生成word
        // File wordOutputFile = new File("D:/pdf/ok_template.docx");
        // OutputStream outputStream = new FileOutputStream(wordOutputFile);
        // wordToPdfUtils.createWord(outputStream);
    }


}
