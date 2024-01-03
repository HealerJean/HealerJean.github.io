package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.enums.CodeEnum;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.exceptions.BusinessException;
import com.healerjean.proj.exceptions.ParameterException;
import com.healerjean.proj.exceptions.PlatformException;
import com.healerjean.proj.exceptions.RpcException;
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
        throw new ParameterException(CodeEnum.ParamsErrorEnum.ERROR_CODE_10002);
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
        throw new BusinessException(CodeEnum.BusinessErrorEnum.ERROR_CODE_20001);
    }

    @ApiOperation("businessExceptionMsg")
    @LogIndex
    @GetMapping("businessExceptionMsg")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> businessExceptionMsg() {
        throw new BusinessException("用户创建失败");
    }



    @ApiOperation("rpcExceptionDefaultEnum")
    @LogIndex
    @GetMapping("rpcExceptionDefaultEnum")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> rpcExceptionDefaultEnum() {
        throw new RpcException(CodeEnum.RpcErrorEnum.ERROR_CODE_USER_0_30001_0001);
    }


    @ApiOperation("rpcExceptionEnumShowMsg")
    @LogIndex
    @GetMapping("rpcExceptionEnumShowMsg")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> rpcExceptionEnumShowMsg() {
        throw new RpcException(CodeEnum.RpcErrorEnum.ERROR_CODE_USER_1_30001_0001, "1000", "用户不存在");
    }


    @ApiOperation("rpcExceptionEnumMsg")
    @LogIndex
    @GetMapping("rpcExceptionEnumMsg")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> rpcExceptionEnumMsg() {
        throw new RpcException(CodeEnum.RpcErrorEnum.ERROR_CODE_USER_1_30001_0001, "1000", "底层结构异常", "用户不存在");
    }



    @ApiOperation("platformException")
    @LogIndex
    @GetMapping("platformException")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> platformException() {
        throw new PlatformException(CodeEnum.PlatformErrorEnum.ERROR_CODE_40001);
    }
}
