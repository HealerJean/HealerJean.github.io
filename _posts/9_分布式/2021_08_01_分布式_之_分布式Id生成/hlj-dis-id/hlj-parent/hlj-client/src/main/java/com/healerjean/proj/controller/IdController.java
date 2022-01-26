package com.healerjean.proj.controller;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.util.id.IdGeneratorSnowflake;
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

import java.util.Date;

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

    @Autowired
    private IdGeneratorSnowflake idGeneratorSnowflake;


    @ApiOperation(value = "snowflake",
            notes = "snowflake",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class)
    @GetMapping(value = "snowflake", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean snowflake() {

        Long id = idGeneratorSnowflake.snowflakeId();
        log.info("id:{}",id);
        //不够64位，前面补0
        String bitsWith0 = String.format("%64s", Long.toBinaryString(id)).replace(" ","0");
        log.info("bits:{}, bitLength:{}",bitsWith0, bitsWith0.length());

        log.info("1位:{},",bitsWith0.substring(0,1));
        String bit41 = bitsWith0.substring(1, 42);
        long timestamp = Long.parseLong(bit41, 2);
        Date date = new Date(timestamp);
        log.info("41位:{},length:{},timeStamp:{},date:{}",bit41,bit41.length(),timestamp, date);

        String bit10 = bitsWith0.substring(42, 52);
        log.info("10位:{},length:{}",bit10, bit10.length());

        String bit12 = bitsWith0.substring(52);
        log.info("12位:{},length:{}",bit12, bit12.length());

        return ResponseBean.buildSuccess(id);
    }

}
