package com.hlj.proj.controller;

import com.hlj.proj.api.demo.DemoEntityService;
import com.hlj.proj.dto.Demo.DemoDTO;
import com.hlj.proj.dto.ResponseBean;
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
@RequestMapping("hlj")
@Slf4j
public class DemoController {

    @Autowired
    private DemoEntityService demoEntityService;

    @ApiOperation(value = "添加demo实体",
            notes = "添加demo实体",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @PostMapping(value = "demo/add", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean insert(DemoDTO demoDTO) {
        log.info("样例--------导入Demo数据------数据信息{}", demoDTO);
        return ResponseBean.buildSuccess(demoEntityService.addDemoEntity(demoDTO));
    }


    @ApiOperation(notes = "根据id查找Demo实体类",
            value = "根据id查找Demo实体类",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "demo主键", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("demo/{id}")
    @ResponseBody
    public ResponseBean findById(@PathVariable Long id) {
        log.info("样例--------查询Demo数据------数据Id{}", id);
        return ResponseBean.buildSuccess(demoEntityService.findById(id));
    }


    @ApiOperation(notes = "所有Demo实体类",
            value = "所有Demo实体类",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping("demos")
    @ResponseBody
    public ResponseBean findAll(DemoDTO demoDTO) {
        log.info("样例--------查询Demo列表数据------数据信息", demoDTO);
        return ResponseBean.buildSuccess(demoEntityService.findAll());
    }

}
