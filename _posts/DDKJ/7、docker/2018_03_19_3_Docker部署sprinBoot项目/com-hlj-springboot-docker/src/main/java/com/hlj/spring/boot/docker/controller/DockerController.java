package com.hlj.spring.boot.docker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@RestController
public class DockerController {


    Logger logger = LoggerFactory.getLogger(DockerController.class);

    @RequestMapping("/")
    public String index() {



        try {
            File fileDirectory = new File("/usr/local/fileDirectory");
            if(!fileDirectory.exists()){
                fileDirectory.mkdirs();
            }
            File file = new File(fileDirectory,"test.jpg");
            FileOutputStream  outputStream = new FileOutputStream(file);
            URL u = new URL("https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WXfdsafasdfadsf.jpeg");
            BufferedImage imageQR = ImageIO.read(u);
            ImageIO.write(imageQR, "jpg", outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        return "Hello Docker! 图片";
    }

}
