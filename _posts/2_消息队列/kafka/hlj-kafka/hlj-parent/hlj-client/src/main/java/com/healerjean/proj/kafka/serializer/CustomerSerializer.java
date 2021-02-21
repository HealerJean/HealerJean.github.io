package com.healerjean.proj.kafka.serializer;

import com.healerjean.proj.kafka.dto.CustomerMsgDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @Author FengZhen
 * @Date 2020-03-30 22:49
 * @Description 自定义序列化器：不建议使用，因为如果修改序列化器，就会出现新旧消息不兼容。
 * 建议使用已有的序列化器和反序列化器，如JSON、Avro、Thrift或Protobuf
 */
public class CustomerSerializer implements Serializer<CustomerMsgDTO> {


    /**
     * Customer对象被序列化成：
     * 表示customerID的4字节整数
     * 表示customerName长度的4字节整数（如果customerName为空，则长度为0）
     * 表示customerName的N个字节
     *
     * @param topic
     * @param data
     * @return
     */
    @Override
    public byte[] serialize(String topic, CustomerMsgDTO data) {
        try {
            byte[] serializedName;
            int stringSize;
            if (null == data) {
                return null;
            } else {
                if (StringUtils.isNotBlank(data.getName())) {
                    serializedName = data.getName().getBytes("UTF-8");
                    stringSize = serializedName.length;
                } else {
                    serializedName = new byte[0];
                    stringSize = 0;
                }
            }
            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + stringSize);
            buffer.putInt(data.getId());
            buffer.putInt(stringSize);
            buffer.put(serializedName);
            return buffer.array();
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Customer to byte[] " + e);
        }
    }



    /**
     * 不做任何配置
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    /**
     * 不需要关闭任何东西
     */
    @Override
    public void close() {
    }
}