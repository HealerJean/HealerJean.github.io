package com.healerjean.proj.statemechine.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * EventExecuteResultEnum
 *
 * @author zhangyujin
 * @date 2023/6/28$  09:50$
 */
public enum EventExecuteResultEnum {

    SUCCESS("success", "成功", true),

    SUCCESS_REPEAT("successRepeat", "成功-重复", true),

    FAIL_TRY_LOCK_ERROR("failTryLock", "失败-获取状态机同步锁失败", false),

    FAIL_CONFIG_EMPTY("failConfigEmpty", "失败-获取不到流转配置", false),

    FAIL_EXECUTE_ERROR("failError", "失败-错误", false),
    ;

    private final String code;

    private final String msg;

    private final Boolean result;

    /**
     * EventExecuteResultEnum
     *
     * @param code   code
     * @param msg    msg
     * @param result success
     * @return {@link }
     */
    EventExecuteResultEnum(String code, String msg, boolean result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }


    /**
     * toEventExecuteResultEnum
     *
     * @param code code
     * @return {@link EventExecuteResultEnum}
     */
    public static EventExecuteResultEnum toEventExecuteResultEnum(String code) {
        return Arrays.stream(EventExecuteResultEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * getResult
     *
     * @param code code
     * @return {@link Boolean}
     */
    public static Boolean getResult(String code) {
        EventExecuteResultEnum eventExecuteResultEnum = toEventExecuteResultEnum(code);
        if (Objects.nonNull(eventExecuteResultEnum)) {
            return eventExecuteResultEnum.getResult();
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getResult() {
        return result;
    }
}
