package com.healerjean.proj.strata.web.vo;

/**
 * CityInfoVO
 *
 * @author zhangyujin
 * @date 2026/3/30
 */

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * 旅游城市信息Bean
 */
@Data
@JsonPropertyOrder({"cityName", "province", "famousScenic", "travelSeason"})
public class CityInfoVO {
    private String cityName; // 城市名称
    private String province; // 所属省份
    private String famousScenic; // 著名景点
    private String travelSeason; // 最佳旅游季节
}