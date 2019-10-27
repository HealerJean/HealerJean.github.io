package com.healerjean.proj.controller;

import com.healerjean.proj.dto.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName DemoController
 * @date 2019/6/13  20:01.
 * @Description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "demo控制器")
@Controller
@RequestMapping("hlj/nginx")
@Slf4j
public class DemoController {


    @GetMapping(value = "path", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean path(String name) {
        return ResponseBean.buildSuccess(name);
    }


    @GetMapping(value = "path/2", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean path2(String name) {
        return ResponseBean.buildSuccess(name);
    }


}
