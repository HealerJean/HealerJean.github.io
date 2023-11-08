package com.healerjean.proj.easyrule.rule;

import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.support.composite.UnitRuleGroup;

/**
 * DemoUnitRuleGroup
 *
 * @author zhangyujin
 * @date 2023/11/6
 */
@Slf4j
public class DemoUnitRuleGroup extends UnitRuleGroup {

    /**
     * DemoUnitRuleGroup
     *
     * @param name name
     * @param description description
     */
    public DemoUnitRuleGroup(String name, String description) {
        super(name, description);
        addRule(new ARule());
        addRule(new BRule());
    }


    /**
     * 重新了该方法，单独规则里的Action 不会执行。如果想要单独规则里的Action也执行，需要在组合规则的excute方法里增加一句 super.execute(facts); 这个需要根据实际需求来选择。
     */
    @Override
    public void execute(Facts facts) throws Exception {
        super.execute(facts);
        Object o = facts.get("person");
        log.info("[DemoUnitRuleGroup#execute] person:{}", JsonUtils.toString(o));
    }
}
