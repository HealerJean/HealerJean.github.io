package com.healerjean.proj.controller;

import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.common.exception.ParameterErrorException;
import com.healerjean.proj.util.json.JsonUtils;
import com.healerjean.proj.util.validate.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName UserInfoController
 * @Author TD
 * @Date 2019/1/9 17:47
 * @Description 用户信息获取
 */
@Controller
@Slf4j
@RequestMapping("/oauth2")
public class UserController {

    /**
     * 获取用户登录信息
     * 1.校验令牌是否有效
     * 2.通过令牌查询数据
     */
    @GetMapping(value = "userinfo")
    @ResponseBody
    public String userInfo(String code)  {
        return "";
    }





}
