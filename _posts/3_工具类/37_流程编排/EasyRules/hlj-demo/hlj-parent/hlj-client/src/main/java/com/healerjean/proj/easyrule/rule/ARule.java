package com.healerjean.proj.easyrule.rule;

import com.healerjean.proj.data.easyrule.Person;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * AgeRule
 *
 * @author zhangyujin
 * @date 2023/11/4
 */

@Slf4j
@Rule(name = "A rule", description = "A description", priority = 2)
public class ARule {

    @Condition
    public boolean when(@Fact("person")Person person) {
        log.info("[ARule#when] when(person.getAge() > 18 result:{})", person.getAge() > 18);
        return person.getAge() > 18;
    }

    @Action(order = 1)
    public void then1(@Fact("person") Person person) {
        log.info("[ARule#then1] success");
    }


    @Action(order = 2)
    public void then2(@Fact("person") Person person) {
        log.info("[ARule#then2] success");
    }

}
