package com.healerjean.proj.ohc;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * StringSerializer
 *
 * @author zhangyujin
 * @date 2025/7/7
 */
// 值的序列化器（String 类型）
public class StringSerializer implements CacheSerializer<String> {

    @Override
    public int serializedSize(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 65535) {
            throw new RuntimeException("String too long");
        }
        return bytes.length + 2; // 2 bytes for length
    }

    @Override
    public void serialize(String value, ByteBuffer buf) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buf.put((byte) ((bytes.length >> 8) & 0xFF));
        buf.put((byte) (bytes.length & 0xFF));
        buf.put(bytes);
    }

    @Override
    public String deserialize(ByteBuffer buf) {
        int length = (((buf.get() & 0xFF) << 8) | (buf.get() & 0xFF));
        byte[] bytes = new byte[length];
        buf.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}