package com.healerjean.proj.controller.core;

import com.healerjean.proj.api.core.UserService;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.config.ConstantsConfig;
import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.UcenterFrontMenuDTO;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.dto.user.VerifyCodeDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.service.system.security.SecurityService;
import com.healerjean.proj.service.system.verifyCode.VerifyCodeService;
import com.healerjean.proj.shiro.Auth2Token;
import com.healerjean.proj.util.IpUtils;
import com.healerjean.proj.util.UserUtils;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName UserController
 * @date 2019/10/18  14:10.
 * @Description
 */
@RestController
@RequestMapping("hlj")
@Api(description = "用户管理")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    @Qualifier(value = "managerUserServiceImpl")
    private UserService userService;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private SecurityService securityService;


    @ApiOperation(value = "用户管理-用户添加",
            notes = "用户管理-用户添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @PostMapping(value = "user/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean addUser(@RequestBody UserDTO userDTO) {
        log.info("用户管理---------用户添加---------请求参数：{}", userDTO);
        String validate = ValidateUtils.validate(userDTO, ValidateGroup.ManageAddUser.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            throw new ParameterErrorException(validate);
        }

        //1、获取随机盐开始准备保存用户
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = secureRandomNumberGenerator.nextBytes().toHex();
        String password = new Md5Hash(userDTO.getPassword(), salt).toString();
        userDTO.setPassword(password);
        userDTO.setSalt(salt);
        userDTO.setUserType(BusinessEnum.UserTypeEnum.管理人员.code);

        //将该登录用户的阿里妈妈信息与该添加的用户形成关联
        LoginUserDTO loginUser = UserUtils.getLoginUser();
        userService.addUser(userDTO, loginUser);
        return ResponseBean.buildSuccess("添加用户成功");
    }


    @ApiOperation(value = "用户管理-用户登陆",
            notes = "用户管理-用户登陆",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = UserDTO.class)
    @PostMapping(value = "user/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        log.info("用户管理---------用户登陆---------请求参数：{}", userDTO);
        String validate = ValidateUtils.validate(userDTO, ValidateGroup.Login.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            throw new ParameterErrorException(validate);
        }
        if (StringUtils.isBlank(userDTO.getEmail()) && StringUtils.isBlank(userDTO.getUserName())) {
            throw new ParameterErrorException("请选择邮箱或者用户名登录");
        }

        //1、图片验证码校验
        HttpSession session = request.getSession();
        VerifyCodeDTO verifyCodeDTO = new VerifyCodeDTO();
        verifyCodeDTO.setType(BusinessEnum.VerifyCodeTypeEnum.图片验证码);
        verifyCodeDTO.setIp(IpUtils.getIp(request));
        verifyCodeDTO.setRandomId(session.getId());
        verifyCodeDTO.setSystemCode(ConstantsConfig.application_name);
        boolean verify = verifyCodeService.verify(verifyCodeDTO, userDTO.getVerifyCode());
        if (!verify) {
            log.info("用户管理--------用户登陆--------验证码：{}；图片验证码不正确：{}", userDTO.getVerifyCode(), verifyCodeDTO);
            throw new ParameterErrorException("图片验证码不正确");
        }

        //2、登陆次数校验以及密码校验
        securityService.loginTimeLimitCheck(userDTO.getUserName());
        userDTO.setUserType(BusinessEnum.UserTypeEnum.管理人员.code);
        UserDTO result = userService.getUserInfo(userDTO);
        String password = new Md5Hash(userDTO.getPassword(), result.getSalt()).toString();
        if (!password.equals(result.getPassword())) {
            throw new ParameterErrorException("用户密码错误");
        }

        //3、用户登录
        Subject subject = SecurityUtils.getSubject();
        Auth2Token auth2Token = new Auth2Token(result.getUserId(), result.getUserName(), IpUtils.getIp(request));
        subject.login(auth2Token);

        //4、登陆成功清空登录次数
        securityService.loginTimeLimitCount(result.getUserName(), true);
        return ResponseBean.buildSuccess(ResponseEnum.登陆成功);
    }


    @ApiOperation(value = "用户管理-用户登出",
            notes = "用户管理-用户登出",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = UserDTO.class
    )
    @GetMapping(value = "user/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean logout() {
        log.info("用户管理--------用户登出--------参数信息信息：{}");
        //shiro登出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseBean.buildSuccess("用户登出成功");
    }


    @ApiOperation(value = "用户管理-当前用户查询",
            notes = "用户管理-当前用户查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @GetMapping(value = "user/current", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean checkCurrentUser() {
        log.info("用户管理--------获取当前用户信息");
        LoginUserDTO loginUser = UserUtils.getLoginUser();
        return ResponseBean.buildSuccess("当前登陆用户查询成功", loginUser);
    }


    @ApiOperation(value = "用户管理-当前用户菜单查询",
            notes = "用户管理-当前用户菜单查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = MenuDTO.class)
    @GetMapping(value = "user/current/menus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean currentUserMenus() {
        log.info("用户管理--------当前用户菜单查询");
        List<MenuDTO> menus = UserUtils.getMenus();
        List<UcenterFrontMenuDTO> result = null;
        result = UserUtils.recursionMenus(menus, result);
        return ResponseBean.buildSuccess("当前用户菜单查询成功", result);
    }

}
