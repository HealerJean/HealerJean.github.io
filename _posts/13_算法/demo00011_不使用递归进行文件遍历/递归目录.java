package com.hlj.arith.demo00011_不使用递归进行文件遍历;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author HealerJean
 * @ClassName 递归目录
 * @date 2020/4/20  15:44.
 * @Description
 */
@Slf4j
public class 递归目录 {


    @Test
    public void test() throws Exception {

        cycleFileContent(new File("D:\\study\\HealerJean.github.io\\_posts"), "img/tctip/weixin.jpg");
        // cycleFileContent(new File("D:\\study\\HealerJean.github.io\\_posts\\3_工具类"), "拦截器和过滤器");
    }

    private static int size = 1;
    public  void cycleFileContent(File file, String content) throws Exception {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                cycleFileContent(f, content);
            } else {
                if (f.getName().endsWith(".md")){
                    FileInputStream fileInputStream = new FileInputStream(f);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    String str = new String(outStream.toByteArray(), "utf-8");
                    if (str.contains(content)) {
                        log.info("第【{}】个文件匹配到内容，路径为：{}", size, f.getPath());
                        size++ ;
                    }
                }

            }
        }
    }
}
