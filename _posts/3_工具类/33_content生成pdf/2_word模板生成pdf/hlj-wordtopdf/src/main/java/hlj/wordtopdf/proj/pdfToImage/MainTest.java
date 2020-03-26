package hlj.wordtopdf.proj.pdfToImage;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName MainTest
 * @date 2020/3/26  10:50.
 * @Description
 */
public class MainTest {


    @Test
    public void test() {
        // PdfToImageUtils.pdf2png("D:", "ccc", "png");
        PdfToImageUtils.pdf2png("D:", "ccc", 0, 3, "png");
    }
}
