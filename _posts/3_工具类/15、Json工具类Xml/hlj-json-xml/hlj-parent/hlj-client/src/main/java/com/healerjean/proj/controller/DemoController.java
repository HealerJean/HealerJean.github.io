package com.healerjean.proj.controller;

import com.healerjean.proj.a_test.json.JsonDemoDTO;
import com.healerjean.proj.a_test.json.jackson.d02_JsonType.Human;
import com.healerjean.proj.common.ValidateGroup;
import com.healerjean.proj.service.DemoEntityService;
import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.dto.Demo.DemoDTO;
import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.common.enums.ResponseEnum;
import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.util.validate.ValidateUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("hlj/demo/json")
@Slf4j
public class DemoController {


    /**
     * {"type":"woman","district":"北京","manField":"男人"}
     */
    @ApiOperation(value = "jsonType",
            notes = "jsonType",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseBean.class)
    @PostMapping(value = "jsonType", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean get(@RequestBody Human human) {
        log.info("样例--------GET请求------数据信息{}", human);
        return ResponseBean.buildSuccess(human);
    }



}
