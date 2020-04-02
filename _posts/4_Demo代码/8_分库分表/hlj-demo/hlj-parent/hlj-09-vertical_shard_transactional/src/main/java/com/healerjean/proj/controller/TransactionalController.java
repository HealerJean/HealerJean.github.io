package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.TransactionalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author HealerJean
 * @ClassName TransactionalController
 * @date 2020/4/2  14:48.
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
@RequestMapping("hlj/transactional")
@Slf4j
public class TransactionalController {


    @Autowired
    private TransactionalService transactionalService;

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
        transactionalService.dbTransactional(userDTO, companyDTO);
        return ResponseBean.buildSuccess("执行成功");
    }


    @ApiOperation(notes = "isolationTransactional",
            value = "isolationTransactional",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = UserDTO.class)
    @PostMapping("isolationTransactional")
    @ResponseBody
    public ResponseBean isolationTransactional(UserDTO userDTO, CompanyDTO companyDTO) {
        log.info("demo--------isolationTransactional------");
        transactionalService.isolationTransactional(userDTO, companyDTO);
        return ResponseBean.buildSuccess("执行成功");
    }
}
