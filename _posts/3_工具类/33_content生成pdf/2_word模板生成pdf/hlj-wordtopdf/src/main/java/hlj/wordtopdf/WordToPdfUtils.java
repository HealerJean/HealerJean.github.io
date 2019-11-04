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
import java.math.BigDecimal;
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
        String BILL = "Bill" ;

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


        List<ContractTeamplateBillDTO> contractTeamplateBillDTOS = new ArrayList<>();
        ContractTeamplateBillDTO contractTeamplateBillDTO = new ContractTeamplateBillDTO();
        contractTeamplateBillDTO.setIndex(1);
        contractTeamplateBillDTO.setBuyerCompanyName("爱酷科技");
        contractTeamplateBillDTO.setBasicTransactionContractNo("基础交易合同及编号1");
        contractTeamplateBillDTO.setBillType("应收账款种类1");
        contractTeamplateBillDTO.setValidAmount("10000.36");
        contractTeamplateBillDTO.setBillEndTime("2019-01-01");
        contractTeamplateBillDTO.setCreditNo("444441");
        contractTeamplateBillDTO.setBillAmount("10.36" );
        contractTeamplateBillDTO.setBillStartTime("2019-02-02");


        ContractTeamplateBillDTO contractTeamplateBillDTO2 = new ContractTeamplateBillDTO();
        contractTeamplateBillDTO2.setIndex(2);
        contractTeamplateBillDTO2.setBuyerCompanyName("爱酷科技2");
        contractTeamplateBillDTO2.setBasicTransactionContractNo("基础交易合同及编号12");
        contractTeamplateBillDTO2.setBillType("应收账款种类12");
        contractTeamplateBillDTO2.setValidAmount("100020.36");
        contractTeamplateBillDTO2.setBillEndTime("2019-02-01");
        contractTeamplateBillDTO2.setCreditNo("4444412");
        contractTeamplateBillDTO2.setBillAmount("10.32" );
        contractTeamplateBillDTO2.setBillStartTime("2019-02-03");

        contractTeamplateBillDTOS.add(contractTeamplateBillDTO);
        contractTeamplateBillDTOS.add(contractTeamplateBillDTO2);

        wordToPdfUtils.addList(BILL, ContractTeamplateBillDTO.class, contractTeamplateBillDTOS);

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
