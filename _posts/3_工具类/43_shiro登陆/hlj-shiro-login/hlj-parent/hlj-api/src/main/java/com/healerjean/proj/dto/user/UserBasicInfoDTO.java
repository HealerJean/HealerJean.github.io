package com.healerjean.proj.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * @author HealerJean
 * @ClassName UserBasicInfoDTO
 * @Date 2019-11-02  00:36.
 * @Description 用户基本信息
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBasicInfoDTO {

    private String userType;
    private String username;
    private String realName;
    private String password;
    private String repeatPassword;
    private String telephone;
    private String email;
}
