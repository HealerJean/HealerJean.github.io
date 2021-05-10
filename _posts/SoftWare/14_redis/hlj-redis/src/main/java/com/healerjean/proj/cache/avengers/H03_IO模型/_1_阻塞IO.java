package com.healerjean.proj.cache.avengers.H03_IO模型;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangyujin
 * @date 2021/5/10  5:40 下午.
 * @description
 */
@Slf4j
public class _1_阻塞IO {


    /**
     * 1、创建 ServerSocket
     * 2、新建一个线程用于接收客户端连接 （伪异步 IO）
     * 3、serverSocket.accept() 建立连接sock连接
     * 4、每一个新来的连接给其创建一个线程去处理
     * 5、阻塞式获取数据直到客户端断开连接
     * 6、读取数据并处理
     */
    @Test
    public void test() throws IOException {
        // 1、创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(9999);
        // 2、新建一个线程用于接收客户端连接 （伪异步 IO）
        new Thread(() -> {
            while (true) {
                log.info("开始阻塞, 等待客户端连接");
                try {
                    // 3、 serverSocket.accept() 建立连接sock连接
                    Socket socket = serverSocket.accept();

                    // 4、每一个新来的连接给其创建一个线程去处理
                    new Thread(() -> {
                        byte[] data = new byte[1024];
                        int len = 0;
                        log.info("客户端连接成功，阻塞等待客户端传入数据");
                        try {
                            // 5、 阻塞式获取数据直到客户端断开连接
                            InputStream inputStream = socket.getInputStream();

                            // 6、读取数据并处理
                            while ((len = inputStream.read(data)) != -1) {
                                log.info(new String(data, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
