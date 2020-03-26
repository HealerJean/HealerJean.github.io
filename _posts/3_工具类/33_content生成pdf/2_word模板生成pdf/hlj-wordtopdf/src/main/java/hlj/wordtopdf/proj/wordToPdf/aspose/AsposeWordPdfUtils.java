package hlj.wordtopdf.proj.wordToPdf.aspose;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName WordUtils
 * @date 2019/10/12  14:00.
 * @Description Word模板生成pdf/word
 */
@Data
@Slf4j
@Service
public class AsposeWordPdfUtils {

    @Value("${fadada.word.pdf_fonts}")
    private String fontPathTemp;
    private static String fontPath;

    @PostConstruct
    public void init() {
        fontPath = fontPathTemp;
    }


    private static final String LICENSESTR = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";

    static {
        // 凭证
        try {
            InputStream license = new ByteArrayInputStream(LICENSESTR.getBytes("UTF-8"));
            License asposeLic = new License();
            asposeLic.setLicense(license);
        } catch (Exception e) {
            throw new RuntimeException("word转pdf凭证验证失败", e);
        }
    }

    public static void word2Pdf(InputStream inputStream, OutputStream outputStream) {
        try {
            Document doc = new Document(inputStream);
            FontSettings fontSettings = FontSettings.getDefaultInstance();
            fontSettings.setFontsFolder(fontPath, false);
            doc.setFontSettings(fontSettings);
            doc.save(outputStream, SaveFormat.PDF);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static XWPFTemplate getDocxFTemplate(InputStream inputStream, Map<String, Object> map) {
        Configure.ConfigureBuilder builder = Configure.newBuilder();
        builder.buildGramer("${", "}");
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(map);
        return template;
    }

    public static void main(String[] args) throws Exception {
        InputStream is = new FileInputStream(new File("C:/Users/tongdong/Desktop/test/test.docx"));
        FileOutputStream fileOS = new FileOutputStream(new File("C:/Users/tongdong/Desktop/test/test.pdf"));
        AsposeWordPdfUtils.word2Pdf(is, fileOS);
    }


}
