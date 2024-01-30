package com.healerjean.proj.controller;

import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.enums.CodeEnum;
import com.healerjean.proj.exceptions.BusinessException;
import com.healerjean.proj.exceptions.ParameterException;
import com.healerjean.proj.exceptions.PlatformException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;

/**
 * ControllerHandleConfig
 *
 * @author zhangyujin
 * @date 2023/6/15  10:55.
 */
@Slf4j
@ControllerAdvice
public class ControllerHandleConfig {

    /**
     * 不支持的请求方始
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public BaseRes<?> methodNotSupportExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("不支持的请求方式", e);
        return BaseRes.buildFailure(CodeEnum.ParamsErrorEnum.ERROR_CODE_101001.getCode(), e.getMessage());
    }


    /**
     * 参数类型错误
     * 1、(BindException : 比如 Integer 传入String  )
     * Field error in object 'demoDTO' on field 'age': rejected value [fasdf]; codes [typeMismatch.demoDTO.age,typeMismatch.age,typeMismatch.java.lang.Integer,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [demoDTO.age,age]; arguments []; default message [age]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Integer' for property 'age'; nested exception is java.lang.NumberFormatException: For input string: "fasdf"]
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseRes<?> bindExceptionHandler(BindException e) {
        log.error("====参数类型错误===", e);
        return BaseRes.buildFailure(CodeEnum.ParamsErrorEnum.ERROR_CODE_100000);
    }


    /**
     * 参数格式问题
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, HttpMessageConversionException.class, UnexpectedTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseRes<?> httpMessageConversionExceptionHandler(Exception e) {
        log.error("====参数格式异常===", e);
        return BaseRes.buildFailure(CodeEnum.ParamsErrorEnum.ERROR_CODE_102000);
    }


    /**
     * 参数错误
     */
    @ExceptionHandler(value = ParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseRes<?> parameterErrorExceptionHandler(ParameterException e) {
        log.error("====参数异常:code:{},msg:{}", e.getCode(), e.getMessage(), e);
        return BaseRes.buildFailure(e.getCode(), e.getMessage());
    }


    /**
     * 业务异常，给前台返回异常数据
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseRes<?> businessExceptionHandler(BusinessException e) {
        log.error("====业务异常:code:{},msg:{},showMsg:{}", e.getCode(), e.getMessage(), e.getShowMsg(), e);
        return BaseRes.buildFailure(e.getCode(), e.getShowMsg());
    }


    /**
     * 运行异常，给前台返回异常数据
     */
    @ExceptionHandler(value = PlatformException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseRes<?> platformExceptionHandler(PlatformException e) {
        log.error("====运行异常:code:{},msg:{},showMsg:{}", e.getCode(), e.getMessage(), e.getShowMsg(), e);
        return BaseRes.buildFailure(e.getCode(), e.getShowMsg());
    }


    /**
     * 所有异常报错
     */
    @ExceptionHandler
    @ResponseBody
    public HttpEntity<BaseRes<?>> allExceptionHandler(HttpServletResponse response, Exception e) {
        log.error("====系统错误===", e);
        response.setStatus(500);
        return returnMessage(BaseRes.buildFailure(CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL));
    }

    /**
     * returnMessage
     *
     * @param baseRes baseRes
     * @return HttpEntity<BaseRes < ?>>
     */
    private HttpEntity<BaseRes<?>> returnMessage(BaseRes<?> baseRes) {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        header.add("Charset", "UTF-8");
        return new HttpEntity<>(baseRes, header);
    }


    /**
     * 参数非法
     * 1、(BindException : 比如 Integer 传入abc  )
     */
    // @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, HttpRequestMethodNotSupportedException.class, HttpMessageConversionException.class, BindException.class, UnexpectedTypeException.class})
    // @ResponseBody
    // public HttpEntity<ResponseBean> httpMessageConversionExceptionHandler(HttpServletResponse response, Exception e) {
    //     log.error("====参数格式异常===", e);
    //     // 等同于 @ResponseStatus(HttpStatus.BAD_REQUEST)
    //     // 但是setStatus 不能比随便设置,最好一般情况下不要和HttpStatus 有重复的，这样有可能会造成没有输出Response body
    //     response.setStatus(ResponseEnum.参数格式异常.code);
    //     return returnMessage(ResponseBean.buildFailure(ResponseEnum.参数格式异常));
    // }
    // @ExceptionHandler(value ={HttpMessageConversionException.class, BindException.class} )
    // @ResponseBody
    // public HttpEntity<ResponseBean> httpMessageConversionExceptionHandler(Exception e) {
    //     log.error("====参数格式异常===", e);
    //     return new ResponseEntity<>(ResponseBean.buildFailure(ResponseEnum.参数格式异常),HttpStatus.BAD_REQUEST);
    // }

}
