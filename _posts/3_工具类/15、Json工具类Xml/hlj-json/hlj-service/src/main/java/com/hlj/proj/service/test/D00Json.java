package com.hlj.proj.service.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.hlj.proj.utils.JsonUtils;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName D00Json
 * @date 2019-08-04  18:09.
 * @Description
 */
public class D00Json {


    @Test
    public   void packageRequestParams() {
        StringBuffer jsonBuffer = new StringBuffer();
        /**
         * 拼接系统参数
         */
        jsonBuffer.append("{");

        jsonBuffer.append("\"name\":");
        jsonBuffer.append("\"");
        jsonBuffer.append("HealerJean");
        jsonBuffer.append("\",");

        jsonBuffer.append("\"age\":");
        jsonBuffer.append("\"\",");

        jsonBuffer.append("\"sex\":");
        jsonBuffer.append("\"男\"");


        jsonBuffer.append("}");

        JsonNode jsonNode = JsonUtils.toJsonNode(jsonBuffer.toString());
        System.out.println(jsonNode);

        // {"name":"HealerJean","age":"","sex":"男"}
    }


}
