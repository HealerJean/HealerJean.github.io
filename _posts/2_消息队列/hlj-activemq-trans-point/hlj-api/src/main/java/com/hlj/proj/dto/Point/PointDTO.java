package com.hlj.proj.dto.Point;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author HealerJean
 * @ClassName PointDTO
 * @date 2019/9/9  15:10.
 * @Description
 */
@Data
public class PointDTO {
    /**
     * 积分表主键
     */
    private Long poingId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 积分数
     */
    private BigDecimal pointAmount;
}
