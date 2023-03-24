package com.healerjean.proj.controller;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.util.id.SnowflakeIdGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangyujin
 * @date 2022/1/26  11:08 上午.
 * @description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "Id控制器")
@Controller
@RequestMapping("hlj/id")
@Slf4j
public class IdController {

    /**
     * snowflakeIdGenerator
     */
    @Autowired(required = false)
    private SnowflakeIdGenerator snowflakeIdGenerator;


    @ApiOperation(value = "snowflake",
            notes = "snowflake",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class)
    @GetMapping(value = "snowflake", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean snowflake() {
        return snowflakeIdGenerator.get();
    }

}
