package com.hlj.proj.controller;


import com.hlj.proj.common.group.ValidateGroup;
import com.hlj.proj.dto.ResponseBean;
import com.hlj.proj.dto.validate.JavaBean;
import com.hlj.proj.enums.ResponseEnum;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  上午10:22.
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
@RestController
@RequestMapping("hlj")
@Slf4j
public class VialidateController {


    @ApiOperation(value = "Post接口",
            notes = "Post接口",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseBean.class
    )
    @PostMapping(value = "validate", produces = "application/json;charset=utf-8")
    public ResponseBean post(@RequestBody JavaBean javaBean) {
        String validate = ValidateUtils.validate(javaBean, ValidateGroup.HealerJean.class);
        if (!"success".equals(validate)) {
            throw new BusinessException(ResponseEnum.参数错误,validate);
        }
        return ResponseBean.buildSuccess(javaBean);
    }


}


