package com.healerjean.proj.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.constant.RegularExpressionConstants;
import com.healerjean.proj.enums.BusinessEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

/**
 * @ClassName VerifyCodeDTO
 * @Author HealerJean
 * @Date 2019/6/3 14:22
 * @Description 验证码传输对象
 */
@ApiModel(description = "登陆用户信息")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyCodeDTO {

    /**
     * 验证码的类型
     */
    private BusinessEnum.VerifyCodeTypeEnum type;

    /**
     * 系统标识：管理系统，前台
     */
    private String systemCode;

    /**
     * 类似sessionID的随机码
     */
    private String randomId;

    /**
     * 调用IP
     */
    private String ip;
    /**
     * 手机号
     */
    @Pattern(regexp = RegularExpressionConstants.TELEPHONE, message = "手机号码不规范")
    private String telephone;
    /**
     * 邮箱
     */
    @Pattern(regexp = RegularExpressionConstants.EMAIL, message = "邮箱不规范")
    private String email;
}
