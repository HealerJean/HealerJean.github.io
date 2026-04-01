package com.healerjean.proj.strata.infra.chat.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * WeatherTool
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@Component
public class WeatherTool {

    @Tool(description = "获取北京当前温度")
    public String getBeiJingWeather(String city) {
        String result = city + "：20℃，晴天";
        System.out.println("【执行工具】北京天气工具 → 返回 " + result);
        return result;
    }


    @Tool(description = "获取上海当前温度")
    public String getShangHaiWeather(String city) {
        String result = city + "：10℃，晴天";
        System.out.println("【执行工具】上海天气工具 → 返回 " + result);
        return result;
    }


}