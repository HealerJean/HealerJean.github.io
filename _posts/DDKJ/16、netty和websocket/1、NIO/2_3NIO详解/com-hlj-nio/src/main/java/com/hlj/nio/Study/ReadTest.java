package com.hlj.nio.Study;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/28  下午3:58.
 */
public class ReadTest {

    static public void main( String args[] ) throws Exception {
        FileInputStream fin = new FileInputStream( "/Users/healerjean/Desktop/test.txt" );

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据到缓冲区
        fc.read(buffer);
        buffer.flip();

        while (buffer.remaining()>0) {
            byte b = buffer.get();
            System.out.print(((char)b)); //读取中文会乱码哦
        }
        fin.close();
    }
}
