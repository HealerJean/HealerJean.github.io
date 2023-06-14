package com.healerjean.proj.utils.log4j2;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * 敏感日志处理
 * @author zhangyujin
 * @date 2023/6/14  21:23
 */
public class SensitiveParameterizedMessageFactory extends AbstractMessageFactory {

    /**
     * Instance of SensitiveParameterizedMessageFactory.
     */
    public static final SensitiveParameterizedMessageFactory INSTANCE = new SensitiveParameterizedMessageFactory();

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8970940216592525651L;

    /**
     * Constructs a message factory.
     */
    public SensitiveParameterizedMessageFactory() {
        super();
    }

    /**
     * Creates {@link SensitiveParameterizedMessage} instances.
     *
     * @param message The message pattern.
     * @param params The message parameters.
     * @return The Message.
     *
     * @see MessageFactory#newMessage(String, Object...)
     */
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new SensitiveParameterizedMessage(message, params);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0) {
        return new SensitiveParameterizedMessage(message, p0);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return new SensitiveParameterizedMessage(message, p0, p1);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4, p5);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7, final Object p8) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7, final Object p8, final Object p9) {
        return new SensitiveParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
}
