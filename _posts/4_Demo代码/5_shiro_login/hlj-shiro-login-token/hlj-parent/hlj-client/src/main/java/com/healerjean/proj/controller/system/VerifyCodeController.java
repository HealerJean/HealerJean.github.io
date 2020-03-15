package com.healerjean.proj.controller.system;

import com.healerjean.proj.api.core.UserService;
import com.healerjean.proj.config.ConstantsConfig;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.dto.user.VerifyCodeDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.service.system.verifyCode.VerifyCodeService;
import com.healerjean.proj.shiro.AuthConstants;
import com.healerjean.proj.util.IpUtils;
import com.healerjean.proj.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ClassName VerifyCodeController
 * @Author TD
 * @Date 2019/6/3 13:41
 * @Description 验证码控制器
 */

@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "系统管理-验证码")
@RestController
@RequestMapping("hlj/sys/verifyCode")
@Slf4j
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    @Qualifier("clientUserServiceImpl")
    private UserService userService;

    /**
     * 图片验证码
     */
    @GetMapping(value = "captcha")
    public void createCaptcha(
            HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try (ServletOutputStream sos = response.getOutputStream()) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            VerifyCodeDTO verifyCodeDTO = new VerifyCodeDTO();
            verifyCodeDTO.setIp(IpUtils.getIp(request));
            HttpSession session = request.getSession();
            verifyCodeDTO.setRandomId(session.getId());
            verifyCodeDTO.setSystemCode(ConstantsConfig.application_name);
            verifyCodeDTO.setType(BusinessEnum.VerifyCodeTypeEnum.图片验证码);
            LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
            byte[] buf = verifyCodeService.generateCaptcha(verifyCodeDTO, loginUserDTO);
            response.setContentLength(buf.length);
            response.setHeader(AuthConstants.HEADER_TOKEN_NAME, session.getId());
            sos.write(buf);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }


    @GetMapping(value = "/{verifyType}")
    public ResponseBean createRandomCode(
            @PathVariable(name = "verifyType") String verifyType, VerifyCodeDTO verifyCodeDTO,
            HttpServletRequest request, HttpServletResponse response) {
        BusinessEnum.VerifyCodeTypeEnum type = BusinessEnum.VerifyCodeTypeEnum.toEnum(verifyType);
        if (StringUtils.isBlank(verifyType) || type == null) {
            //参数检验失败
            log.error("获取验证码参数校验失败");
            throw new ParameterErrorException("verifyType不存在");
        }
        if (verifyCodeDTO == null) {
            //参数检验失败
            log.error("获取验证码参数校验失败");
            throw new ParameterErrorException("请传输手机号或者邮箱");
        }
        switch (type) {
            case 注册邮箱验证码:
            case 找回密码邮箱验证码:
                if (StringUtils.isBlank(verifyCodeDTO.getEmail())) {
                    log.error("获取验证码参数校验失败");
                    throw new ParameterErrorException("请传输邮箱");
                }
                break;

        }
        verifyCodeDTO.setIp(IpUtils.getIp(request));
        HttpSession session = request.getSession();
        verifyCodeDTO.setRandomId(session.getId());
        verifyCodeDTO.setSystemCode(ConstantsConfig.application_name);
        verifyCodeDTO.setType(type);
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        verifyCodeService.generateCaptcha(verifyCodeDTO, loginUserDTO);

        response.setHeader(AuthConstants.HEADER_TOKEN_NAME, session.getId());
        return ResponseBean.buildSuccess("验证码发送成功");
    }


    /**
     * 找回密码获取手机或邮箱验证码
     */
    @GetMapping(value = "retrievePassword/{verifyType}")
    public ResponseBean createRetrievePasswordRandomCode(
            @PathVariable(name = "verifyType") String verifyType, UserDTO userDTO,
            HttpServletRequest request, HttpServletResponse response) {
        BusinessEnum.VerifyCodeTypeEnum type = BusinessEnum.VerifyCodeTypeEnum.toEnum(verifyType);
        if (StringUtils.isBlank(verifyType) || type == null) {
            //参数检验失败
            log.error("获取验证码参数校验失败");
            throw new ParameterErrorException("verifyType不存在");
        }
        userDTO = userService.getUserInfo(userDTO);
        if (userDTO == null) {
            //参数检验失败
            log.error("获取验证码参数校验失败，用户不存在");
            throw new BusinessException(ResponseEnum.用户不存在);
        }
        VerifyCodeDTO verifyCodeDTO = new VerifyCodeDTO();
        verifyCodeDTO.setEmail(userDTO.getEmail());
        verifyCodeDTO.setTelephone(userDTO.getTelephone());
        verifyCodeDTO.setIp(IpUtils.getIp(request));
        HttpSession session = request.getSession();
        verifyCodeDTO.setRandomId(session.getId());
        verifyCodeDTO.setSystemCode(ConstantsConfig.application_name);
        verifyCodeDTO.setType(type);
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        verifyCodeService.generateCaptcha(verifyCodeDTO, loginUserDTO);
        response.setHeader(AuthConstants.HEADER_TOKEN_NAME, session.getId());
        return ResponseBean.buildSuccess("验证码发送成功");
    }
}
