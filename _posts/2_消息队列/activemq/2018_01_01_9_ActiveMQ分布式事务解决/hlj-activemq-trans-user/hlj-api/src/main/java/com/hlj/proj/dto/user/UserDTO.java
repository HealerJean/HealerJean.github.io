package com.hlj.proj.dto.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author HealerJean
 * @ClassName UserDTO
 * @date 2019/9/9  14:21.
 * @Description
 */
@Data
public class UserDTO {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 积分
     */
    private BigDecimal pointAmount;

}
