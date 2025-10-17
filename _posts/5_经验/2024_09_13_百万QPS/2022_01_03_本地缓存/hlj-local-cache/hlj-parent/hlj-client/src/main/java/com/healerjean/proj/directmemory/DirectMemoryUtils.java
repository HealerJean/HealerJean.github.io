package com.healerjean.proj.directmemory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * DirectmemoryUtils
 *
 * @author zhangyujin
 * @date 2025/10/16
 */
public final class DirectMemoryUtils {


    /**
     * 将位图文件加载到ByteBuffer
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static ImmutableRoaring64Split32Bitmap buildBySingleFile(String filePath) throws IOException {
        FileChannel fileChannel = null;
        ByteBuffer targetByteBuffer = null;
        try{
            fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
            targetByteBuffer = ByteBuffer.allocateDirect((int) fileChannel.size());
            fileChannel.read(targetByteBuffer);
            ByteBufferUtils.flip(targetByteBuffer);
            return new ImmutableRoaring64Split32Bitmap(targetByteBuffer);
        } finally {
            IoUtils.closeNoErr(fileChannel);
        }


    /**
     * 取除数
     */
    public static int split32BitCount(long val){
        return (int) (val >>> 32);
    }


    /**
     *  按照2^32取余数  即：2的32次方-1=4294967296-1=4294967295
     */
    public static long unSignedIntMaxToLongVal = ((long)1<<32)-1L;
    public static int split32BitOffset(long val){
        return (int) ((val) & unSignedIntMaxToLongVal);
    }


}
