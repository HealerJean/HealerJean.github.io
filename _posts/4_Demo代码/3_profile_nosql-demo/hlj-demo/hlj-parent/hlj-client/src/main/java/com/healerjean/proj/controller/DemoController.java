package com.healerjean.proj.controller;

import com.healerjean.proj.api.demo.DemoEntityService;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.dto.Demo.DemoDTO;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.exception.BusinessException;
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
@RequestMapping("hlj")
@Slf4j
public class DemoController {

    @Autowired
    private DemoEntityService demoEntityService;


    @ApiOperation(value = "demo实体",
            notes = "demo实体",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping(value = "demo/get", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean get(DemoDTO demoDTO) {
        log.info("样例--------GET请求------数据信息{}", demoDTO);
        String validate = ValidateUtils.validate(demoDTO, ValidateGroup.HealerJean.class);
        if (!validate.equals(CommonConstants.COMMON_SUCCESS)){
            throw new BusinessException(ResponseEnum.参数错误,validate);
        }
        return ResponseBean.buildSuccess(demoEntityService.getMmethod(demoDTO));
    }



}
