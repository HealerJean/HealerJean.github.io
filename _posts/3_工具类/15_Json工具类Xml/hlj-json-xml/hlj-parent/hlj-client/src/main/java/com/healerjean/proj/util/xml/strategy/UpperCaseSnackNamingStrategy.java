package com.healerjean.proj.util.xml.strategy;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName UpperCaseSnackNamingStrategy
 * @Date 2019/10/29  22:48.
 * @Description 转大写并加下划线
 */
public class UpperCaseSnackNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
    @Override
    public String translate(String input) {
        if (input == null) {
            return input;
        }
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (i > 0 || c != '_') {
                if (Character.isUpperCase(c)) {
                    if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                        result.append('_');
                        resultLength++;
                    }
                    wasPrevTranslated = true;
                } else {
                    wasPrevTranslated = false;
                }
                c = Character.toUpperCase(c);
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }
}
