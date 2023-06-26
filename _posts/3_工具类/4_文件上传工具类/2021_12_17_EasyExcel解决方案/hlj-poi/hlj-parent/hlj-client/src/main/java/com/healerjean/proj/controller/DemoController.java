package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.service.DemoEntityService;
import com.healerjean.proj.service.DemoExcelService;
import com.healerjean.proj.service.ReadExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
@RequestMapping("hlj/demo")
@Slf4j
public class DemoController {

    @Autowired
    private DemoEntityService demoEntityService;
    @Autowired
    private DemoExcelService demoExcelService;

    @ApiOperation(value = "excelDataReady",
            notes = "excelDataReady",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "excelDataReady", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean excelDataReady() throws Exception {
        demoEntityService.demoEntityBigDataInsert();
        return ResponseBean.buildSuccess();
    }



    @ApiOperation(value = "exportBigDataExcel",
            notes = "exportBigDataExcel",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "exportBigDataExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportBigDataExcel(HttpServletResponse response) throws Exception {
        demoExcelService.exportBigDataExcel(response);
    }


}
