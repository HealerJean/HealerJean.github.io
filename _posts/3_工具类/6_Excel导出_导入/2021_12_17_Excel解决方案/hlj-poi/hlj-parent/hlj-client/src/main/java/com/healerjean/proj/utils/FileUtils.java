package com.healerjean.proj.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * FileUtils
 * 文件相关工具
 *
 * @author zhanghanlin6
 * @date 2022/2/23
 */
@Slf4j
public class FileUtils {


    public static InputStream readeInputStreamByUrl(String linkUrl) {
        try {
            URL url = new URL(linkUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(60 * 1000);
            //读取数据时间为5秒
            conn.setReadTimeout(60 * 1000);
            //通过输入流获取数据
            return new BufferedInputStream(conn.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("文件流读取异常:" + e.getMessage());
        }
    }

}
