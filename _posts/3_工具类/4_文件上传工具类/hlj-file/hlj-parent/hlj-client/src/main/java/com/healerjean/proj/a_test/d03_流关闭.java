package com.healerjean.proj.a_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author HealerJean
 * @ClassName d03_流关闭
 * @date 2019/11/9  17:31.
 * @Description
 */
@Slf4j
public class d03_流关闭 {


    // 1、流的正确关闭姿势

    /**
     * 1.1、之finaly 中关闭流
     */
    @Test
    public void test1() {

        File file = new File("/Users/healerjean/Desktop/test/file.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            //TODO 操作代码
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("fileOutputStream未正确关闭");
                }
            }
        }
    }


    /**
     * 1.2、应该在循环中关闭流，如下
     */
    @Test
    public void closeStream2() {
        for (int i = 0; i < 100; i++) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream("/Users/healerjean/Desktop/test/file.txt");
                //TODO 操作代码
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        log.error("fileOutputStream未正确关闭");
                    }
                }
            }
        }
    }


    /**
     * 1.3、java7解决关闭流新姿势
     * 只要实现的自动关闭接口(Closeable)的类都可以在try结构体上定义，java会自动帮我们关闭，及时在发生异常的情况下也会。可以在try结构体上定义多个，用分号隔开即可,如：
     */
    @Test
    public void java6CloseStream() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("/Users/healerjean/Desktop/test/file.txt"); FileInputStream fileInputStream = new FileInputStream("/Users/healerjean/Desktop/test/file.txt");) {
            //TODO 操作代码
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }







}



