package com.hlj.moudle.demo.controller;


import com.hlj.config.PropertySourceT;
import com.hlj.data.general.AppException;
import com.hlj.data.general.ResponseBean;
import com.hlj.utils.PropertiesUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  上午10:22.
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "获取properties")
@Controller
@RequestMapping("properties")
@Slf4j
public class DemoController {

    @Autowired
    private PropertySourceT propertySourceT ;

    @ApiOperation(value = "获取properties",notes = "获取properties",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "参数", required =false,paramType = "query", dataType = "string")
    })
    @GetMapping("get")
    @ResponseBody
    public ResponseBean get(String name){
        try {
            System.out.println(propertySourceT.getAge());
            System.out.println(PropertiesUtil.getProperty(name));
            return ResponseBean.buildSuccess(PropertiesUtil.getProperty(name));
        } catch (AppException e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getMessage());
        }
    }

}
