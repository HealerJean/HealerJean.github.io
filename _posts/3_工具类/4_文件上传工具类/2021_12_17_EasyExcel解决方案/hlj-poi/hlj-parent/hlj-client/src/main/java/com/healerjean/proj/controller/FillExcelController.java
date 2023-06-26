package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.service.FillExcelService;
import com.healerjean.proj.service.ReadExcelService;
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
 * @date 2021/12/17  5:46 下午.
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
@Api(description = "demo控制器")
@Controller
@RequestMapping("hlj/excel/fill")
@Slf4j
public class FillExcelController {

    @Autowired
    private FillExcelService fillExcelService;

    @ApiOperation(value = "1、最简单的填充",
            notes = "1、最简单的填充",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "simpleFill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean simpleFill() throws Exception {
        fillExcelService.simpleFill();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "2、填充列表",
            notes = "2、填充列表",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "listFill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean listFill()   {
        fillExcelService.listFill();
        return ResponseBean.buildSuccess();
    }



    @ApiOperation(value = "3、复杂的填充",
            notes = "3、复杂的填充",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "complexFill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean complexFill()   {
        fillExcelService.complexFill();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "4、数据量大的复杂填充",
            notes = "4、数据量大的复杂填充",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "complexFillWithTable", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean complexFillWithTable()   {
        fillExcelService.complexFillWithTable();
        return ResponseBean.buildSuccess();
    }



    @ApiOperation(value = "5、横向填充",
            notes = "5、横向填充",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "horizontalFill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean horizontalFill()   {
        fillExcelService.horizontalFill();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "6、多列表组合填充",
            notes = "6、多列表组合填充",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "compositeFill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean compositeFill()   {
        fillExcelService.compositeFill();
        return ResponseBean.buildSuccess();
    }
}
