package com.healerjean.proj.controller;

import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.common.dto.ValidateGroup;
import com.healerjean.proj.common.enums.ResponseEnum;
import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.CompanyService;
import com.healerjean.proj.service.UserService;
import com.healerjean.proj.utils.validate.ValidateUtils;
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
@RequestMapping("hlj/company")
@Slf4j
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "insert",
            notes = "insert",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @PostMapping(value = "insert", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean insert(CompanyDTO companyDTO) {
        log.info("user--------insert------请求参数：{}", companyDTO);
        return ResponseBean.buildSuccess(companyService.insert(companyDTO));
    }


    @ApiOperation(notes = "findById",
            value = "findById",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "demo主键", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("findById/{id}")
    @ResponseBody
    public ResponseBean findById(@PathVariable Long id) {
        log.info("company--------findById------id：{}", id);
        return ResponseBean.buildSuccess(companyService.findById(id));
    }



    @ApiOperation(notes = "list",
            value = "list",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @GetMapping("list")
    @ResponseBody
    public ResponseBean list() {
        log.info("company--------list------");
        return ResponseBean.buildSuccess(companyService.list());
    }

}
