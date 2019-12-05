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
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 组装map参数到合同模板中
     */
    public void assembleMap(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof List) {
                List list = (List) value;
                addList(key, list.get(0).getClass(), list);
            } else if (value instanceof String) {
                addText(key, value.toString());
            } else if (value instanceof File) {
                addImage(key, (File) value);
            } else {
                addObject(key, value.getClass(), value);
            }
        }
    }

    /**
     * 普通文本
     */
    public void addText(String key, Object text) {
        this.iContext.put(key, text);
    }


    /**
     * 普通对象
     */
    public void addObject(String key, Class<?> c, Object o) {
        try {
            this.fieldsMetadata.load(key, c);
            this.iContext.put(key, o);
        } catch (XDocReportException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * List对象
     */
    public void addList(String key, Class<?> c, List list) {
        try {
            this.fieldsMetadata.load(key, c, true);
            this.iContext.put(key, list);
        } catch (XDocReportException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 图片
     */
    public void addImage(String key, File file) {
        this.fieldsMetadata.addFieldAsImage(key);
        IImageProvider img = new FileImageProvider(file);
        this.iContext.put(key, img);
    }

    public void addImage(String key, File file, float width, float height) {
        this.fieldsMetadata.addFieldAsImage(key);
        IImageProvider img = new FileImageProvider(file, true);
        img.setSize(width, height);
        this.iContext.put(key, img);
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
            this.ixDocReport.convert(this.iContext, options, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * word(比较胡扯，只支持docx) 转 pdf
     */
    public static void wordConverterToPdf(InputStream source, OutputStream target) throws Exception {
        XWPFDocument doc = new XWPFDocument(source);
        PdfConverter.getInstance().convert(doc, target, null);
    }


    /**
     * 替换word中的字符串
     *
     * @param map      其中，key--替换的标记    value--替换的值
     */
    public static void replaceAll(InputStream source, OutputStream target, Map<String, String> map) {
        try {
            XWPFDocument doc = new XWPFDocument(source);

            //处理段落
            //------------------------------------------------------------------
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        boolean isSetText = false;
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (text.indexOf(key) != -1) {
                                isSetText = true;
                                text = text.replaceAll(key, value);
                            }
                            if (isSetText) {
                                run.setText(text, 0);
                            }
                        }

                    }

                }
            }

            //------------------------------------------------------------------

            //处理表格
            //------------------------------------------------------------------
            List<XWPFTable> tables = doc.getTables();
            for (XWPFTable table : tables) {
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {

                        String text = cell.getText();
                        if (text != null) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                if (text.equals(key)) {
                                    //删除原单元格值
                                    cell.removeParagraph(0);
                                    //设置新单元格的值
                                    cell.setText(value);
                                }
                            }
                        }
                    }

                }
            }

            PdfConverter.getInstance().convert(doc, target, null);

            //------------------------------------------------------------------
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            File template = new File("D:/pdf/template.docx");
            WordToPdfUtils wordToPdfUtils = new WordToPdfUtils(new FileInputStream(template));
            Map<String, Object> map = new HashMap<>();
            // 1、普通字符
            map.put("word", "helloWord");
            map.put("nihao", "nihao");
            map.put("kkk", "kkk");

            //  2、对象
            Person person = new Person("HealerJean", "25", "男");
            map.put("person", person);

            // 3、List表格
            Table table1 = new Table("11", "12", "13");
            Table table2 = new Table("21", "22", "23");
            List<Table> table = new ArrayList<>();
            table.add(table1);
            table.add(table2);
            map.put("table", table);
            // 4、 图片
            map.put("img", new File("D:/pdf/img.png"));

            //组装数据
            wordToPdfUtils.assembleMap(map);

            //生成pdf
            File outputFile = new File("D:/pdf/ok_template.pdf");
            OutputStream outputStream = new FileOutputStream(outputFile);
            wordToPdfUtils.createPdf(outputStream);

            // 生成word
            // File wordOutputFile = new File("D:/pdf/ok_template.docx");
            // OutputStream outputStream = new FileOutputStream(wordOutputFile);
            // wordToPdfUtils.createWord(outputStream);




        } catch (Exception e) {
            throw new RuntimeException("word模板生成文件错误", e);
        }
    }


}
