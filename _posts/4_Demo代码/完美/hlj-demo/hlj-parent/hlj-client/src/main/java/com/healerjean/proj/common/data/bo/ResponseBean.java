package com.healerjean.proj.common.data.bo;


import com.healerjean.proj.common.enums.CodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回对象
 */
@Accessors(chain = true)
@Data
public class ResponseBean<T> {

    private ResponseBean() {
    }

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 返回结果
     */
    private T data;

    /**
     * msg
     */
    private String msg = "";

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
    public static <T> ResponseBean<T> buildSuccess() {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(true);
        responseBean.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        return responseBean;
    }

    /**
     * buildSuccess
     *
     * @param data data
     * @param <T>  <T>
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildSuccess(T data) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(true);
        responseBean.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        responseBean.setData(data);
        return responseBean;
    }

    /**
     * buildSuccess
     *
     * @param <T> <T>
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildSuccess(T data, String msg) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(true);
        responseBean.setData(data);
        responseBean.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_SUCCESS.getCode());
        responseBean.setMsg(msg);
        return responseBean;
    }

    /**
     * buildFailure
     *
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildFailure() {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(false);
        responseBean.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL.getCode());
        return responseBean;
    }


    /**
     * parameterErrorEnum
     *
     * @param msg msg
     * @param <T> <T>
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildParamsFailure(String msg) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(false);
        responseBean.setCode(CodeEnum.ErrorCodeEnum.ERROR_CODE_PARAMS_ERROR.getCode());
        responseBean.setMsg(msg);
        return responseBean;
    }

    /**
     * parameterErrorEnum
     *
     * @param codeEnum codeEnum
     * @param <T>      <T>
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildFailure(CodeEnum codeEnum) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(false);
        if (codeEnum instanceof CodeEnum.ErrorCodeEnum) {
            CodeEnum.ErrorCodeEnum errorEnum = (CodeEnum.ErrorCodeEnum) codeEnum;
            responseBean.setCode(errorEnum.getCode());
            responseBean.setMsg(errorEnum.getMsg());
            return responseBean;
        }
        if (codeEnum instanceof CodeEnum.BusinessErrorEnum) {
            CodeEnum.BusinessErrorEnum errorEnum = (CodeEnum.BusinessErrorEnum) codeEnum;
            responseBean.setCode(errorEnum.getCode());
            responseBean.setMsg(errorEnum.getMsg());
            return responseBean;
        }
        if (codeEnum instanceof CodeEnum.ParamsErrorEnum) {
            CodeEnum.ParamsErrorEnum errorEnum = (CodeEnum.ParamsErrorEnum) codeEnum;
            responseBean.setCode(errorEnum.getCode());
            responseBean.setMsg(errorEnum.getMsg());
            return responseBean;
        }
        return responseBean;
    }

    /**
     * @param code code
     * @param msg  msg
     * @param <T>  <T>
     * @return ResponseBean
     */
    public static <T> ResponseBean<T> buildFailure(String code, String msg) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(false);
        responseBean.setCode(code);
        responseBean.setMsg(msg);
        return responseBean;
    }

}
