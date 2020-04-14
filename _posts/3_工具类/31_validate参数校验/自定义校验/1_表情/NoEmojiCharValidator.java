package com.fintech.confin.web.utils.validate.validator;

import com.fintech.confin.web.utils.validate.anno.NoEmojiChar;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName NoEmojiCharValidator
 * @Author DYB
 * @Date 2020/4/14 10:05
 * @Description
 * @Version V1.0
 */
public class NoEmojiCharValidator implements ConstraintValidator<NoEmojiChar, String> {

    @Override
    public void initialize(NoEmojiChar constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)){
            return true;
        }
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char codePoint = value.charAt(i);
            if (!isNotEmojiCharacter(codePoint)) {
                //判断到了这里表明，确认有表情字符
                return false;
            }
        }
        return true;

    }

    /**
     * 判断是否为非Emoji字符
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
