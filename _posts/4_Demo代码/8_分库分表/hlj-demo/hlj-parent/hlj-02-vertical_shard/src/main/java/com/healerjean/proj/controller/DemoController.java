package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.DemoEntityService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
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
@RequestMapping("hlj/demo")
@Slf4j
public class DemoController {

    @Autowired
    private DemoEntityService demoEntityService;


    @ApiOperation(value = "insert",
            notes = "insert",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @PostMapping(value = "insert", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean insert(DemoDTO demoDTO) {
        log.info("demo--------insert------请求参数：{}", demoDTO);
        return ResponseBean.buildSuccess(demoEntityService.insert(demoDTO));
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
        log.info("demo--------findById------id：{}", id);
        return ResponseBean.buildSuccess(demoEntityService.findById(id));
    }



    @ApiOperation(notes = "list",
            value = "list",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @GetMapping("list")
    @ResponseBody
    public ResponseBean list() {
        log.info("demo--------list------");
        return ResponseBean.buildSuccess(demoEntityService.list());
    }

    @ApiOperation(notes = "dbTransactional",
            value = "dbTransactional",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @PostMapping("dbTransactional")
    @ResponseBody
    public ResponseBean dbTransactional(UserDTO userDTO, CompanyDTO companyDTO) {
        log.info("demo--------dbTransactional------");
        TransactionTypeHolder.set(TransactionType.XA);
        demoEntityService.dbTransactional(userDTO, companyDTO);
        return ResponseBean.buildSuccess("执行成功");
    }

}
