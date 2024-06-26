package com.healerjean.proj.common.data.bo;


import com.healerjean.proj.common.enums.CodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回对象
 */
@Accessors(chain = true)
@Data
public class BaseRes<T> {

    public BaseRes() {
    }

    /**
     * 返回结果
     */
    private T data;

    /**
     * msg
     */
    private String msg;

    /**
     * Code
     */
    private String code;


    /**
     * buildSuccess
     *
     * @param <T> <T>
     * @return ResponseBean
     */
    public static <T> BaseRes<T> buildSuccess() {
        BaseRes<T> baseRes = new BaseRes<>();
        baseRes.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        baseRes.setMsg(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getMsg());
        return baseRes;
    }

    /**
     * buildSuccess
     *
     * @param data data
     * @param <T>  <T>
     * @return ResponseBean
     */
    public static <T> BaseRes<T> buildSuccess(T data) {
        BaseRes<T> baseRes = new BaseRes<>();
        baseRes.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        baseRes.setMsg(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getMsg());
        baseRes.setData(data);
        return baseRes;
    }

    /**
     * buildSuccess
     *
     * @param <T> <T>
     * @return ResponseBean
     */
    public static <T> BaseRes<T> buildSuccess(T data, String msg) {
        BaseRes<T> baseRes = new BaseRes<>();
        baseRes.setData(data);
        baseRes.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        baseRes.setMsg(msg);
        return baseRes;
    }


    /**
     * parameterErrorEnum
     *
     * @param codeEnum codeEnum
     * @param <T>      <T>
     * @return ResponseBean
     */
    public static <T> BaseRes<T> buildFailure(CodeEnum codeEnum) {
        BaseRes<T> baseRes = new BaseRes<>();
        baseRes.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL.getCode());
        baseRes.setMsg(CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL.getMsg());

        if (codeEnum instanceof CodeEnum.BusinessErrorEnum) {
            CodeEnum.BusinessErrorEnum errorEnum = (CodeEnum.BusinessErrorEnum) codeEnum;
            baseRes.setCode(errorEnum.getCode());
            baseRes.setMsg(errorEnum.getShowMsg());
            return baseRes;
        }
        if (codeEnum instanceof CodeEnum.ParamsErrorEnum) {
            CodeEnum.ParamsErrorEnum errorEnum = (CodeEnum.ParamsErrorEnum) codeEnum;
            baseRes.setCode(errorEnum.getCode());
            return baseRes;
        }
        return baseRes;
    }

    /**
     * @param code code
     * @param msg  msg
     * @param <T>  <T>
     * @return ResponseBean
     */
    public static <T> BaseRes<T> buildFailure(String code, String msg) {
        BaseRes<T> baseRes = new BaseRes<>();
        baseRes.setCode(code);
        baseRes.setMsg(msg);
        return baseRes;
    }

    public  boolean isSuccess(){
        return  CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode().equals(this.getCode());
    }

}
