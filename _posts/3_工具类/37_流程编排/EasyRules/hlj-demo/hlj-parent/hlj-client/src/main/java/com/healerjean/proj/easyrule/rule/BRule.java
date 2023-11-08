package com.healerjean.proj.easyrule.rule;

import com.healerjean.proj.data.easyrule.Person;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * GenderRule
 *
 * @author zhangyujin
 * @date 2023/11/4
 */
@Slf4j
@Rule(name = "B rule", description = "B description", priority = 1)
public class BRule {

    @Condition
    public boolean when(@Fact("person")Person person) {
        log.info("[BRule#when] when(person.getGender().equals(\"male\")) result:{})", person.getGender().equals("male"));
        return person.getGender().equals("male");
    }

    @Action(order = 1)
    public void then1(@Fact("person") Person person) {
        int i = 1/0;
        log.info("[BRule#then1] success");
    }


    @Action(order = 2)
    public void then2(@Fact("person") Person person) {
        log.info("[BRule#then2] success");
    }

}