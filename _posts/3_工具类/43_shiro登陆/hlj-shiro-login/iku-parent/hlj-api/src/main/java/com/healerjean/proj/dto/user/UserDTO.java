package com.healerjean.proj.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.RegularExpressionConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author HealerJean
 * @ClassName UserDTO
 * @date 2019/10/18  13:53.
 * @Description
 */
@ApiModel(description = "用户DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {


    @ApiModelProperty(value = "用户主键")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空", groups = {ValidateGroup.RegisterUser.class, ValidateGroup.ManageAddUser.class})
    private String userName;

    @ApiModelProperty(value = "用户身份类型")
    private String userType;

    @ApiModelProperty(value = "用户真实姓名")
    @NotBlank(message = "用户真实姓名不能为空", groups = { ValidateGroup.RegisterUser.class, ValidateGroup.ManageAddUser.class})
    private String realName;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空", groups = { ValidateGroup.RegisterUser.class, ValidateGroup.ManageAddUser.class})
    private String telephone;

    @ApiModelProperty(value = "有效")
    @NotBlank(message = "有效不能为空", groups = { ValidateGroup.RegisterUser.class, ValidateGroup.ManageAddUser.class})
    @Pattern(regexp = RegularExpressionConstants.EMAIL, message = "请输入正确的邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空", groups = { ValidateGroup.RegisterUser.class, ValidateGroup.Login.class, ValidateGroup.ManageAddUser.class})
    @Pattern(regexp = RegularExpressionConstants.PASSWORD, message = "密码强度过低(长度至少 6 位，由大小写字母和数字共同组成)")
    private String password;

    @ApiModelProperty(value = "密码")
    private String oldPassword;

    @ApiModelProperty(value = "重复密码")
    private String repeatPassword;

    @ApiModelProperty(value = "随机盐")
    private String salt;


    @ApiModelProperty(value = "图片验证码")
    @NotBlank(message = "图片验证码不能为空", groups = {ValidateGroup.RegisterUser.class, ValidateGroup.Login.class})
    @Length(message = "图片验证码长度不能大于6", max = 6)
    private String verifyCode;

    @ApiModelProperty(value = "邮箱验证码")
    @Length(message = "邮箱验证码长度不能大于6", max = 6, groups = {ValidateGroup.RegisterUser.class})
    private String mailCode;

}
