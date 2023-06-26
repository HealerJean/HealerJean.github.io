package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
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
@RequestMapping("hlj/excel/read")
@Slf4j
public class ReadExcelController {

    @Autowired
    private ReadExcelService readExcelService;


    @ApiOperation("1、最简单的读")
    @ResponseBody
    @GetMapping(value = "simpleRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean simpleRead(String filePath)  {
        readExcelService.simpleRead(filePath);
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "2、指定列的下标或者列名",
            notes = "2、指定列的下标或者列名",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "indexOrNameRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean indexOrNameRead(String filePath) throws Exception {
        readExcelService.indexOrNameRead(filePath);
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "3、读多个或者全部sheet",
            notes = "3、读多个或者全部sheet",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "repeatedRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean repeatedRead() throws Exception {
        readExcelService.repeatedRead();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "4、日期、数字、自定义格式转化",
            notes = "4、日期、数字、自定义格式转化",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "converterRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean converterRead() throws Exception {
        readExcelService.converterRead();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "5、多行头 (默认1行，这里指定有2行标题)",
            notes = "5、多行头 (默认1行，这里指定有2行标题)",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "complexHeaderRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean complexHeaderRead() throws Exception {
        readExcelService.complexHeaderRead();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "6、读取表头数据",
            notes = "6、读取表头数据",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "headerRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean headerRead() throws Exception {
        readExcelService.headerRead();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "7、额外信息（批注、超链接、合并单元格信息读取）",
            notes = "7、额外信息（批注、超链接、合并单元格信息读取）",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "extraRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean extraRead() throws Exception {
        readExcelService.extraRead();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "8、数据转化等异常处理",
            notes = "8、数据转化等异常处理",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "exceptionRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean exceptionRead() throws Exception {
        readExcelService.exceptionRead();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "9、同步的返回，不推荐使用，如果数据量大会把数据放到内存里面",
            notes = "9、同步的返回，不推荐使用，如果数据量大会把数据放到内存里面",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "synchronousRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean synchronousRead() throws Exception {
        readExcelService.synchronousRead();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "10、不创建对象的读",
            notes = "10、不创建对象的读",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "noModelRead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean noModelRead() throws Exception {
        readExcelService.noModelRead();
        return ResponseBean.buildSuccess();
    }




}
