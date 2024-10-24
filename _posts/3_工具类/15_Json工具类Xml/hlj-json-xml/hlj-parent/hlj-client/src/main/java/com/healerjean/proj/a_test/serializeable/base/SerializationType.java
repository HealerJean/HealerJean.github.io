package com.healerjean.proj.a_test.serializeable.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SerializationType
 *
 * @author zhangyujin
 * @date 2024/10/22
 */

@Getter
@AllArgsConstructor
public enum SerializationType {

    JDK((byte) 1),

    JSON((byte) 2),

    MSGPACK((byte) 3),

    HESSIAN((byte) 4),

    PROTOSTUFF((byte) 5),

    KRYO((byte) 6),

    ;

    private final byte code;

    public static SerializationType fromCode(byte code) {
        for (SerializationType protocolType : values()) {
            if (protocolType.code == code) {
                return protocolType;
            }
        }
        return null;
    }

}