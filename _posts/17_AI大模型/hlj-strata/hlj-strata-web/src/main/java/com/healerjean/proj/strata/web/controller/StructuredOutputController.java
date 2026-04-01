package com.healerjean.proj.strata.web.controller;

/**
 * StructuredOutputController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */

import com.healerjean.proj.strata.web.vo.CityInfoVO;
import com.healerjean.proj.strata.web.vo.WeatherInfoVO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 结构化输出转换器实战Controller
 */
@RestController
public class StructuredOutputController {
    private final ChatClient chatClient;

    // 构造注入ChatClient
    public StructuredOutputController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 案例1：基础Bean转换 - 获取城市天气信息
     *
     * @param city 城市名称
     * @return 结构化WeatherInfo对象
     */
    @GetMapping("/ai/weather")
    public WeatherInfoVO getWeather(@RequestParam String city) {
        // ChatClient自动使用BeanOutputConverter，无需手动创建
        return chatClient.prompt()
                .system("你是专业的天气查询助手，严格按指定JSON格式返回天气信息，无多余解释")
                .user(u -> u.text("请查询{city}的当前天气信息，包含温度、天气状况、风向风力、出行建议")
                        .param("city", city))
                .call()
                .entity(WeatherInfoVO.class);
    }


    /**
     * 案例2：泛型List Bean转换 - 获取热门旅游城市
     *
     * @param count 城市数量
     * @return 结构化List<CityInfo>对象
     */
    @GetMapping("/ai/travel/cities")
    public List<CityInfoVO> getTravelCities(@RequestParam(defaultValue = "3") Integer count) {
        // 使用ParameterizedTypeReference解决泛型擦除
        ParameterizedTypeReference<List<CityInfoVO>> typeRef = new ParameterizedTypeReference<List<CityInfoVO>>() {
        };
        return chatClient.prompt()
                .system("你是专业的旅游顾问，严格按指定JSON格式返回旅游城市信息，无多余解释，返回数量严格为{count}个")
                .user(u -> u.text("推荐{count}个国内热门旅游城市，包含所属省份、著名景点、最佳旅游季节").param("count", count))
                .call()
                .entity(typeRef);
    }


    /**
     * 案例3：MapOutputConverter - 动态Map转换 - 获取水果营养信息
     *
     * @param fruit 水果名称
     * @return 结构化Map<String, Object>对象
     */
    @GetMapping("/ai/fruit/nutrition")
    public Map<String, Object> getFruitNutrition(@RequestParam String fruit) {
        MapOutputConverter typeRef =  new MapOutputConverter();
        return chatClient.prompt()
                .system("你是专业的营养师，严格按RFC8259标准返回JSON格式的水果营养信息，无多余解释，key为营养成分，value为含量/作用")
                .user(u -> u.text("请介绍{fruit}的主要营养成分和含量").param("fruit", fruit))
                .call()
                .entity(typeRef); // 转换为Map对象
    }

    /**
     * 案例4：ListOutputConverter - 简单List转换 - 获取成语列表
     *
     * @param count 成语数量
     * @param theme 成语主题
     * @return 结构化List<String>对象
     */
    @GetMapping("/ai/idiom/list")
    public List<String> getIdiomList(@RequestParam(defaultValue = "5") Integer count,
                                     @RequestParam String theme) {
        // 创建ListOutputConverter，使用Spring默认转换服务
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());
        return chatClient.prompt()
                .system("你是专业的语文老师，严格返回纯逗号分隔的列表，无序号、无多余解释，数量严格为{count}个")
                .user(u -> u.text("推荐{count}个关于{theme}的成语")
                        .param("count", count)
                        .param("theme", theme))
                .call()
                .entity(listOutputConverter); // 转换为List<String>
    }

    /**
     * 案例5：手动创建转换器
     *
     * @param city 城市名称
     * @return 结构化WeatherInfo对象
     */
    @GetMapping("/ai/manualWeather")
    public WeatherInfoVO getManualWeather(@RequestParam String city) {
        BeanOutputConverter<WeatherInfoVO> converter = new BeanOutputConverter<>(WeatherInfoVO.class);

        // 2. 创建模板对象
        PromptTemplate promptTemplate = new PromptTemplate("请查询{city}的当前天气信息，包含温度、天气状况、风向风力、出行建议");
        // 3. 准备参数模型
        Map<String, Object> model = Map.of("city", city);

        // 4. 渲染模板并构建 Prompt
        Prompt prompt = promptTemplate.create(model);

        String context = chatClient.prompt(prompt).call().content();
        return  converter.convert(context);
    }


}