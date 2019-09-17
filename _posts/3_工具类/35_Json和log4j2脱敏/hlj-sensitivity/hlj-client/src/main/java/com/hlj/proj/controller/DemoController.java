package com.hlj.proj.controller;

import com.hlj.proj.api.demo.DemoEntityService;
import com.hlj.proj.dto.Demo.DemoDTO;
import com.hlj.proj.dto.ResponseBean;
import com.hlj.proj.utils.JsonUtils;
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

    /**
     * 正常JsonUtils
     */
    @ApiOperation(value = "正常JsonUtils",
            notes = "正常JsonUtils",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping(value = "sensitivity/normal", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String normal(DemoDTO demoDTO) {
        log.info("脱敏--------Json工具脱敏------数据信息{}", demoDTO);
        demoEntityService.getDemoDTO(demoDTO);
        return JsonUtils.toJsonString(demoDTO);
    }



    /**
     * 建议使用工具类
     *
     * @param demoDTO
     * @return
     */
    @ApiOperation(value = "Json工具脱敏：建议使用",
            notes = "Json工具脱敏：：建议使用",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping(value = "sensitivity/jsonUtils", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sensitivity(DemoDTO demoDTO) {
        log.info("脱敏--------Json工具脱敏------数据信息{}", demoDTO);
        demoEntityService.getDemoDTO(demoDTO);
        return ResponseBean.buildSensitivitySuccess(demoDTO);
    }


    /**
     * 不建议使用注解
     *
     * @param demoDTO
     * @return
     */
    @ApiOperation(value = "注解脱敏：不建议使用",
            notes = "注解脱敏：不建议使用",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping(value = "sensitivity/anno", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean anno(DemoDTO demoDTO) {
        log.info("脱敏--------注解脱敏------数据信息{}", demoDTO);
        demoEntityService.getDemoDTO(demoDTO);
        return ResponseBean.buildSuccess(demoDTO);
    }


    /**
     * 日志脱敏
     *
     * @param demoDTO
     * @return
     */
    @ApiOperation(value = "日志脱敏",
            notes = "日志脱敏",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping(value = "log/sensitivity", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean log(DemoDTO demoDTO) {
        log.info("脱敏--------日志脱敏------脱敏数据{}", demoDTO);
        return ResponseBean.buildSuccess(demoDTO);
    }


}
