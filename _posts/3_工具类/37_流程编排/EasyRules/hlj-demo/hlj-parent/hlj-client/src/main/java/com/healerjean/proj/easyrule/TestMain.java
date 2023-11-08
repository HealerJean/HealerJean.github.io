package com.healerjean.proj.easyrule;

import com.healerjean.proj.data.easyrule.Person;
import com.healerjean.proj.easyrule.rule.*;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.junit.jupiter.api.Test;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2023/11/4
 */
public class TestMain {


    @Test
    public void test02() {
        // 1、定义规则
        Rule weatherRule = new RuleBuilder()
                .name("weather rule")
                .description("if it rains then take an umbrella")
                .when(facts -> facts.get("rain").equals(true))
                .then(facts -> System.out.println("It rains, take an umbrella!"))
                .build();
        Rules rules = new Rules();
        rules.register(weatherRule);

        // 2、定义事实
        Facts facts = new Facts();
        facts.put("rain", true);

        // 3、执行规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
    }

    /**
     * 组合规则1：UnitRuleGroup
     */
    @Test
    public void testUnitRuleGroup() {
        // 1、组合规则封装
        DemoUnitRuleGroup myUnitRuleGroup = new DemoUnitRuleGroup("UnitRuleGroup", "unit of ARule and BRule");

        // 2、组合规则注册
        Rules rules = new Rules();
        rules.register(myUnitRuleGroup);

        // 3、定义事件
        Facts facts = new Facts();
        facts.put("person", new Person()
                .setName("tom")
                .setAge(19)
                .setGender("male"));

        // 4、执行
        RulesEngine adultEngine = new DefaultRulesEngine();
        adultEngine.fire(rules, facts);
    }


    /**
     * 组合规则2：ActivationRuleGroup
     */
    @Test
    public void testDemoActivationRuleGroup() {
        // 1、组合规则封装
        DemoActivationRuleGroup myUnitRuleGroup = new DemoActivationRuleGroup("ActivationRuleGroup", "unit of ARule and BRule");

        // 2、组合规则注册
        Rules rules = new Rules();
        rules.register(myUnitRuleGroup);

        // 3、定义事件
        Facts facts = new Facts();
        facts.put("person", new Person()
                .setName("tom")
                .setAge(19)
                .setGender("male"));

        // 4、执行
        RulesEngine adultEngine = new DefaultRulesEngine();
        adultEngine.fire(rules, facts);
    }

    /**
     * 组合规则2：ConditionalRuleGroup
     */
    @Test
    public void testDemoConditionalRuleGroup() {
        // 1、组合规则封装
        DemoConditionalRuleGroup myUnitRuleGroup = new DemoConditionalRuleGroup("ConditionalRuleGroup", "unit of ARule and BRule");

        // 2、组合规则注册
        Rules rules = new Rules();
        rules.register(myUnitRuleGroup);

        // 3、定义事件
        Facts facts = new Facts();
        facts.put("person", new Person()
                .setName("tom")
                .setAge(19)
                .setGender("male"));

        // 4、执行
        RulesEngine adultEngine = new DefaultRulesEngine();
        adultEngine.fire(rules, facts);
    }


    /**
     * 执行引擎1：skipOnFirstAppliedRule
     */
    @Test
    public void testRulesEngineParameters() {
        // 1、规则注册
        Rules rules = new Rules();
        rules.register(new ARule());
        rules.register(new BRule());

        // 2、定义事件
        Facts facts = new Facts();
        facts.put("person", new Person()
                .setName("tom")
                .setAge(19)
                .setGender("male"));

        // 3、执行
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstFailedRule(true);
        RulesEngine adultEngine = new DefaultRulesEngine(parameters);
        adultEngine.fire(rules, facts);
    }


}
