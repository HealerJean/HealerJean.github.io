package com.healerjean.proj.strata.web.controller;

/**
 * AiMultimodalController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */


import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.ai.content.Media.Format.IMAGE_PNG;

/**
 * Spring AI多模态实战Controller
 */
@RestController
@RequestMapping("/ai/multimodal")
public class AiMultimodalController {

    @Resource
    private ChatClient chatClient;


    /**
     * 案例：解析网络图片（URI）
     *
     * @param imageUrl 网络图片URL
     */
    @GetMapping("/image/url/parse")
    public String parseUrlImage(String imageUrl) {
        try {
            // 1. 构建网络图片URI资源
            UrlResource urlResource = new UrlResource(imageUrl);
            // 2. 多模态交互（自动识别图片MimeType为JPEG/PNG）
            return chatClient.prompt()
                    .system("你是专业的图像解析助手，简洁描述图片核心内容")
                    .user(u -> u.text("请描述这张图片的核心内容")
                            .media(MediaType.IMAGE_JPEG, urlResource)) // 适配JPEG格式
                    .call()
                    .content();
        } catch (Exception e) {
            return "图片解析失败：" + e.getMessage();
        }
    }

}