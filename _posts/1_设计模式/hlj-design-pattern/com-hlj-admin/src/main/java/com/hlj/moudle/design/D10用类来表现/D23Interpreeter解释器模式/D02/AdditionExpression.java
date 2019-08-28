package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;

/**
 * 加法运算解释器
 */
public class AdditionExpression extends CommonAbstractExpression {

    public AdditionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        super(exp1, exp2);
    }

    @Override
    public int interpret() {
        return exp1.interpret()+exp2.interpret();
    }
}
