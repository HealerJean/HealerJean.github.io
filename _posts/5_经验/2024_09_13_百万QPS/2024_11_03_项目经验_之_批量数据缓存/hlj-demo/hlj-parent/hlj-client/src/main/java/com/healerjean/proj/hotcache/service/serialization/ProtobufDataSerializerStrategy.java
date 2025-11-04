package com.healerjean.proj.hotcache.service.serialization;

/**
 * ProtobufSerializer
 *
 * @author zhangyujin
 * @date 2025/11/3
 */

import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.healerjean.proj.hotcache.enums.DataSerializerStrategyEnum;

import java.io.IOException;
import java.io.OutputStream;

public class ProtobufDataSerializerStrategy<T extends Message> implements DataSerializerStrategy<T> {
    private final Parser<T> parser;

    @Override
    public DataSerializerStrategyEnum getDataSerializerStrategyEnum() {
        return DataSerializerStrategyEnum.PROTOBUF;
    }

    public ProtobufDataSerializerStrategy(Parser<T> parser) {
        this.parser = parser;
    }

    @Override
    public void write(T record, OutputStream out) throws IOException {
        record.writeDelimitedTo(out);
    }

    @Override
    public T read(java.io.InputStream in) throws IOException {
        return parser.parseDelimitedFrom(in);
    }

    @Override
    public byte[] serialize(T record) throws IOException {
        return record.toByteArray();
    }

    @Override
    public T deserialize(byte[] data) throws IOException {
        return parser.parseFrom(data);
    }
}
