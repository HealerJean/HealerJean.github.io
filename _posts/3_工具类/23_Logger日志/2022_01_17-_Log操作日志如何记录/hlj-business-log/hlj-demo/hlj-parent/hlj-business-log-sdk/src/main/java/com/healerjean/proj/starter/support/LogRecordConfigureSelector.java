package com.healerjean.proj.starter.support;

import com.healerjean.proj.starter.annotation.EnableLogRecord;
import com.healerjean.proj.starter.configuration.LogRecordProxyAutoConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.lang.Nullable;

/**
 * 如果并不确定引入哪个配置类，需要根据@Import注解所标识的类或者另一个注解(通常是注解)里的定义信息选择配置类的话，用这种方式。
 *
 * AdviceModeImportSelector抽象类实现了SelectImports接口，并限定选择条件只能是AdviceMode枚举类，也就是说你自定义的注解必须包含AdviceMode属性
 */
public class LogRecordConfigureSelector extends AdviceModeImportSelector<EnableLogRecord> {

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{LogRecordProxyAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{"com.healerjean.proj.starter.configuration.LogRecordProxyAutoConfiguration"};
            default:
                return null;
        }
    }
}