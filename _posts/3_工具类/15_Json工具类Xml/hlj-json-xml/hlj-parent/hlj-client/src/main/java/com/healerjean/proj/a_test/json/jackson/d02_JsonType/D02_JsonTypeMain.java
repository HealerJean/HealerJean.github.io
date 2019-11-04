package com.healerjean.proj.a_test.json.jackson.d02_JsonType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healerjean.proj.a_test.json.jackson.d02_JsonType.Human.Man;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @author HealerJean
 * @ClassName D02_JsonTypeMain
 * @date 2019/10/28  10:50.
 * @Description
 */
@Slf4j
public class D02_JsonTypeMain {


    @Test
    public void normal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Man man = new Man();
        man.setManField("男人");
        man.setDistrict("山西");
        String json = mapper.writeValueAsString(man);
        System.out.println(json);
        // {"district":"山西","manField":"男人"}
        //报错 子类转父类，再不能直接序列化为子类
        man = ((Man) mapper.readValue(json, Human.class));
    }


    /**
     *
     */
    @Test
    public void testOne() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Man man = new Man();
        man.setManField("男人");
        man.setDistrict("北京");
        // man.setType("1");

        String manJson = mapper.writeValueAsString(man);
        log.info("序列化Man ：【 {} 】", manJson);
        // manJson = "{\"type\":\"man\",\"district\":\"北京\",\"type\":\"1\",\"manField\":\"男人\"} ";
        Human human = mapper.readValue(manJson, Human.class);
        log.info("子类转父类 ======================");
        String humanJson = mapper.writeValueAsString(human);
        log.info("反序列化man -> Human ：【 {} 】", humanJson);
        log.info("human.getDistrict()  ：【 {} 】", human.getDistrict());
        // log.info("human.getType()  ：【 {} 】",  human.getType());
    }
}
