package com.duodian.youhui.admin.config;

import com.duodian.youhui.admin.utils.ExceptionLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.sound.sampled.Line;


@Slf4j
@ControllerAdvice
public class ControllerExceptionConfig {


    @Autowired
    ResourceUrlProvider resourceUrlProvider;

    /**
     * 用来给ftl模板提供项目路径访问，这种方式不可以使用EnableWebMvc
     * @return
     */
    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

    //配合application中的配置使用哦
    @ExceptionHandler(value = Throwable.class) //处理抛出的异常。一般都是抛出的哦
    @ResponseBody
    public String handler(Throwable t){
        ExceptionLogUtils.log(t,this.getClass());
        return  "SYSTEM ERROR/404/403/等）";
    }



}
