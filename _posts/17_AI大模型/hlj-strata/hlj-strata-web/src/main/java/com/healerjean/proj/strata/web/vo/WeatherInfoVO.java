package com.healerjean.proj.strata.web.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * WeatherInfoVO
 *
 * @author zhangyujin
 * @date 2026/3/30
 */

/**
 * 天气信息Bean
 */
@Data
public class WeatherInfoVO {
    /**
     * 城市名称
     */
    private String city;
    /**
     * 温度
     */
    private String temperature;

    /**
     * 天气状况（晴/雨/多云）
     */
    private String weather;

    /**
     * 风向风力
     */
    private String wind;

    /**
     * 出行建议
     */
    private String tips;
}