package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;


/**
 * 仅仅为了解释数字
 */
public class NumExpression extends ArithmeticExpression {
    private int num;

    public NumExpression(int num) {
        this.num = num;
    }

    @Override
    public int interpret() {
        return num;
    }
}
