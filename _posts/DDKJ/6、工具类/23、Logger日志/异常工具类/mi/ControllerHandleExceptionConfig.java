package com.fintech.client.config;

import com.fintech.scf.common.enums.SystemEnum;
import com.fintech.scf.common.exception.ParameterErrorException;
import com.fintech.scf.common.exception.PasswordErrorException;
import com.fintech.scf.common.exception.ScfException;
import com.fintech.scf.common.exception.UserNotFoundException;
import com.fintech.scf.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ControllerHandleExceptionConfig
 * @date 2019/5/31  20:19.
 * @Description
 */
@Slf4j
@ControllerAdvice
public class ControllerHandleExceptionConfig {

    @ExceptionHandler(value = ParameterErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String ParameterErrorExceptionHandler(ParameterErrorException e) {
        log.error("参数错误------------" + e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("msg", e.getMsg());
        return JsonUtils.toJsonString(map);
    }

    /**
     * 用户不存在
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = UserNotFoundException.class)
    public HttpEntity<String> userNotFoundExceptionHandler(HttpServletResponse response, UserNotFoundException e) {
        String msg = e.getMsg();
        log.error("====用户不存在：{}===", msg);
        Map<String, String> map = new HashMap<>(2);
        map.put("msg", "用户不存在");
        response.setStatus(SystemEnum.ResponseEnum.USER_NOT_FOUND.getCode());
        return returnMessage(JsonUtils.toJsonString(map));
    }


    /**
     * 密码错误
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = PasswordErrorException.class)
    public HttpEntity<String> passwordErrorExceptionHandler(HttpServletResponse response, PasswordErrorException e) {
        String msg = e.getMsg();
        log.error("====密码错误===", msg);
        Map<String, String> map = new HashMap<>(2);
        map.put("msg", "密码错误");
        response.setStatus(SystemEnum.ResponseEnum.PASSWORD_ERROR.getCode());
        return returnMessage(JsonUtils.toJsonString(map));
    }



    /**
     * 所有异常报错
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = ScfException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<String> scfExceptionHandler(HttpServletResponse response, ScfException e) {
        log.error("====SCF异常===", e);
        Map<String, String> map = new HashMap<>(2);
        int code = e.getCode();
        String msg = e.getMsg();
        map.put("msg", msg);
        response.setStatus(code);
        return returnMessage(JsonUtils.toJsonString(map));
    }

    /**
     * 所有异常报错
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<String> allExceptionHandler(HttpServletResponse response, Exception e) {
        String msg = "系统错误";
        log.error("====系统错误===", e);
        Map<String, String> map = new HashMap<>(2);
        map.put("msg", msg);
        response.setStatus(SystemEnum.ResponseEnum.SYSTEM_ERROR.getCode());
        return returnMessage(JsonUtils.toJsonString(map));
    }

    private HttpEntity<String> returnMessage(String content) {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        header.add("Charset", "UTF-8");
        return new HttpEntity<>(content, header);
    }
}
