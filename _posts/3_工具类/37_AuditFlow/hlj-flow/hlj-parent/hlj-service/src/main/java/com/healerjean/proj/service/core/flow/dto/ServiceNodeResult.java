package com.healerjean.proj.service.core.flow.dto;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ServiceNodeResult
 * @Date 2019/11/11  16:05.
 * @Description 流程结果
 */
public class ServiceNodeResult<T> {

    private T data;
    private ServiceResultStatusEnum status;

    public static <T> ServiceNodeResult<T> success(T data) {
        ServiceNodeResult<T> result = new ServiceNodeResult();
        result.data = data;
        result.status = ServiceResultStatusEnum.成功;
        return result;
    }

    public static <T> ServiceNodeResult<T> fail(T data) {
        ServiceNodeResult<T> result = new ServiceNodeResult();
        result.data = data;
        result.status = ServiceResultStatusEnum.失败;
        return result;
    }

    public static <T> ServiceNodeResult<T> suspend(T data) {
        ServiceNodeResult<T> result = new ServiceNodeResult();
        result.data = data;
        result.status = ServiceResultStatusEnum.暂停;
        return result;
    }


    public enum ServiceResultStatusEnum {
        成功("10", "成功"),
        失败("90", "失败"),
        暂停("20", "暂停"),
        ;

        public String code;
        public String desc;

        ServiceResultStatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
