package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.enums.CodeEnum;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.exceptions.BusinessException;
import com.healerjean.proj.exceptions.ParameterException;
import com.healerjean.proj.exceptions.PlatformException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ExceltionController
 *
 * @author zhangyujin
 * @date 2024/1/3
 */
@RestController
@RequestMapping("hlj/exception")
@Api(tags = "Exception-控制器")
@Slf4j
public class ExceptionController {


    @ApiOperation("parameterExceptionEnum")
    @LogIndex
    @GetMapping("parameterExceptionEnum")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> parameterExceptionEnum() {
        throw new ParameterException(CodeEnum.ParamsErrorEnum.ERROR_CODE_100000);
    }


    @ApiOperation("parameterExceptionMsg")
    @LogIndex
    @GetMapping("parameterExceptionMsg")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> parameterExceptionMsg() {
        throw new ParameterException("用户Id不能为空");
    }



    @ApiOperation("businessExceptionEnum")
    @LogIndex
    @GetMapping("businessExceptionEnum")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> businessExceptionEnum() {
        throw new BusinessException(CodeEnum.BusinessErrorEnum.ERROR_CODE_300000);
    }


    @ApiOperation("businessExceptionShowMessage")
    @LogIndex
    @GetMapping("businessExceptionShowMessage")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> businessExceptionShowMessage() {
        throw new BusinessException(CodeEnum.BusinessErrorEnum.ERROR_CODE_301000, "订单创建失败，我是失败原因");
    }

    @ApiOperation("platformException")
    @LogIndex
    @GetMapping("platformException")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> platformException() {
        throw new PlatformException(CodeEnum.PlatformErrorEnum.ERROR_CODE_200000);
    }


    @ApiOperation("platformExceptionShowMessage")
    @LogIndex
    @GetMapping("platformExceptionShowMessage")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> platformExceptionShowMessage() {
        throw new PlatformException(CodeEnum.PlatformErrorEnum.ERROR_CODE_201001, "任务处理失败");
    }


    @ApiOperation("exception")
    @LogIndex
    @GetMapping("exception")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> exception() {
        throw new RuntimeException("系统异常");
    }



}
