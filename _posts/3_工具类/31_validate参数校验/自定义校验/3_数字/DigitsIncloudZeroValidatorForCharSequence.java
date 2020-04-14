package com.fintech.confin.web.utils.validate.validator;

import com.fintech.confin.web.utils.validate.anno.DigitsIncloudZero;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @ClassName DigitsIncloudZeroValidatorForCharSequence
 * @Author TD
 * @Date 2019/7/9 10:47
 * @Description 校验数字整数位和小数位，小数位为0时可接受
 */
public class DigitsIncloudZeroValidatorForCharSequence implements ConstraintValidator<DigitsIncloudZero, CharSequence> {

    private int maxIntegerLength;
    private int maxFractionLength;

    @Override
    public void initialize(DigitsIncloudZero constraintAnnotation) {
        this.maxIntegerLength = constraintAnnotation.integer();
        this.maxFractionLength = constraintAnnotation.fraction();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        if ( charSequence == null ) {
            return true;
        }
        boolean integerFlag = false;
        boolean fractionFlag = false;
        if ( maxIntegerLength < 0 ) {
            integerFlag = true;
        }
        if ( maxFractionLength < 0 ) {
            fractionFlag = true;
        }
        if(integerFlag && fractionFlag){
            return true;
        }
        BigDecimal bigNum = getBigDecimalValue( charSequence );
        if(!integerFlag) {
            int integerPartLength = bigNum.precision() - bigNum.scale();
            integerFlag = maxIntegerLength >= integerPartLength;
        }
        if(!fractionFlag) {
            int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();
            fractionFlag =  maxFractionLength >= fractionPartLength || 0 == bigNum.setScale(0,BigDecimal.ROUND_DOWN).compareTo(bigNum);
        }
        return integerFlag & fractionFlag;
    }

    private BigDecimal getBigDecimalValue(CharSequence charSequence) {
        BigDecimal bd;
        try {
            bd = new BigDecimal( charSequence.toString() );
        }
        catch ( NumberFormatException nfe ) {
            return null;
        }
        return bd;
    }
}
