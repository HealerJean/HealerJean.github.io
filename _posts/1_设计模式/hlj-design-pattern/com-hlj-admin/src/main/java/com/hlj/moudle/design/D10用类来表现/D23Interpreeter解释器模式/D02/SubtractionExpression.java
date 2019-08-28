package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;

/**
 * @author HealerJean
 * @ClassName SubtractionExpression
 * @date 2019/8/23  16:23.
 * @Description
 */
public class SubtractionExpression extends CommonAbstractExpression {


    public SubtractionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        super(exp1, exp2);
    }

    @Override
    public int interpret() {
        return exp1.interpret() - exp2.interpret();
    }
}
