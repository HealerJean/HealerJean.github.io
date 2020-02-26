package com.healerjean.proj.service.system.verifyCode;


import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.VerifyCodeDTO;

/**
 * @ClassName VerifyCodeService
 * @Author TD
 * @Date 2019/6/3 14:01
 * @Description 验证码
 */
public interface VerifyCodeService {

    /**
     * 创建验证码
     */
    byte[] generateCaptcha(VerifyCodeDTO verifyCodeDTO, LoginUserDTO loginUserDTO);
    /**
     * 校验验证码，校验后失效
     */
    boolean verify(VerifyCodeDTO verifyCodeDTO, String verifyCode);
    /**
     * 删除验证码
     */
    void deleteVerifyCode(VerifyCodeDTO verifyCodeDTO);
}
