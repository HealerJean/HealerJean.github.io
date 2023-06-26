package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.service.WriteExcelService;
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
 * @date 2021/12/20  3:31 下午.
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
@RequestMapping("hlj/excel/write")
@Slf4j
public class WriteExcelController {

    @Autowired
    private WriteExcelService writeExcelService;

    @ApiOperation(value = "1、最简单的写",
            notes = "1、最简单的写",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "simpleWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean simpleWrite() throws Exception {
        writeExcelService.simpleWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "2、根据参数只导出指定列",
            notes = "2、根据参数只导出指定列",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "excludeOrIncludeWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean excludeOrIncludeWrite( ) throws Exception {
        writeExcelService.excludeOrIncludeWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "3、指定写入的列",
            notes = "3、指定写入的列",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "indexWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean indexWrite( ) throws Exception {
        writeExcelService.indexWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "4、复杂头写入",
            notes = "4、复杂头写入",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "complexHeadWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean complexHeadWrite( ) throws Exception {
        writeExcelService.complexHeadWrite();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "5、重复多次写入",
            notes = "5、重复多次写入",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "repeatedWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean repeatedWrite( ) throws Exception {
        writeExcelService.repeatedWrite();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "6、日期、数字或者自定义格式转换",
            notes = "6、日期、数字或者自定义格式转换",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "converterWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean converterWrite( ) throws Exception {
        writeExcelService.converterWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "7、图片导出",
            notes = "7、图片导出",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "imageWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean imageWrite( ) throws Exception {
        writeExcelService.imageWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "8、根据模板写入",
            notes = "8、根据模板写入",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "templateWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean templateWrite( ) throws Exception {
        writeExcelService.templateWrite();
        return ResponseBean.buildSuccess();
    }



    @ApiOperation(value = "9、 列宽、行高",
            notes = "9、 列宽、行高",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "widthAndHeightWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean widthAndHeightWrite( ) throws Exception {
        writeExcelService.widthAndHeightWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "10、 注解形式自定义样式",
            notes = "10、 注解形式自定义样式",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "annotationStyleWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean annotationStyleWrite( ) throws Exception {
        writeExcelService.annotationStyleWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "11、 合并单元格",
            notes = "11、 合并单元格",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "mergeWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean mergeWrite( ) throws Exception {
        writeExcelService.mergeWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "12、 自动列宽(不太精确)",
            notes = "12、 自动列宽(不太精确)",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "longestMatchColumnWidthWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean longestMatchColumnWidthWrite( ) throws Exception {
        writeExcelService.longestMatchColumnWidthWrite();
        return ResponseBean.buildSuccess();
    }


    @ApiOperation(value = "13、 动态头，实时生成头写入",
            notes = "13、 动态头，实时生成头写入",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "dynamicHeadWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean dynamicHeadWrite( ) throws Exception {
        writeExcelService.dynamicHeadWrite();
        return ResponseBean.buildSuccess();
    }

    @ApiOperation(value = "14、不创建对象的写",
            notes = "14、不创建对象的写",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @GetMapping(value = "noModelWrite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean noModelWrite( ) throws Exception {
        writeExcelService.noModelWrite();
        return ResponseBean.buildSuccess();
    }


}
