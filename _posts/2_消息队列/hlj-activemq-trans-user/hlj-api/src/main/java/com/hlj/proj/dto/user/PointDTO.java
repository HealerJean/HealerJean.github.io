package com.hlj.proj.dto.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author HealerJean
 * @ClassName PointDTO
 * @date 2019/9/9  14:26.
 * @Description
 */
@Data
public class PointDTO {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 积分
     */
    private BigDecimal pointAmount;

}
