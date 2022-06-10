package com.healerjean.proj.a_test.json.jackson.d03_选择性过滤;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Function;
import com.healerjean.proj.a_test.json.JsonDemoDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HealerJean
 * @date 2020/9/16  14:21.
 * @description
 */
public class MainTestMain {

    @Test
    public void test1() throws JsonProcessingException {
        System.out.println(ReflectionFieldNameUtils.getFieldName(JsonDemoDTO::getCode));
    }


    /**
     * 选择性过滤
     */
    @Test
    public void test3() throws JsonProcessingException {
        JsonDemoDTO jsonDemoDTO = JsonDemoDTO.jsonDemo();
        JsonFilterUtils jsonFilterUtils = new JsonFilterUtils(JsonDemoDTO.class);
        System.out.println(jsonFilterUtils.toJsonString(jsonDemoDTO));

        JsonFilterUtils utils2 = new JsonFilterUtils(JsonDemoDTO.class);
        utils2.include(JsonDemoDTO::getReqSn, JsonDemoDTO::getCode);
        System.out.println(utils2.toJsonString(jsonDemoDTO));

        JsonFilterUtils utils3 = new JsonFilterUtils(JsonDemoDTO.class);
        utils3.filter(JsonDemoDTO::getReqSn, JsonDemoDTO::getCode);
        System.out.println(utils3.toJsonString(jsonDemoDTO));
    }

}
