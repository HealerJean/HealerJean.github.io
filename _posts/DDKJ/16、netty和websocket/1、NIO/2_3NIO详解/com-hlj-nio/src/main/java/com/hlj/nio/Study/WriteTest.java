package com.hlj.nio.Study;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/28  下午3:54.
 */
public class WriteTest {


    static public void main( String args[] ) throws Exception {
        String messageString = "HealerJean";
        byte message[] = messageString.getBytes("UTF-8");
        FileOutputStream fout = new FileOutputStream( "/Users/healerjean/Desktop/test.txt" );
        FileChannel fc = fout.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate( 1024 );

        for (int i=0; i<message.length; ++i) {
            buffer.put( message[i]); //byte[i]
        }
        buffer.flip();
        fc.write( buffer );
        fout.close();
    }
}
