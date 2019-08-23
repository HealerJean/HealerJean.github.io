package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;

/**
 * @author HealerJean
 * @ClassName CommonAbstractExpression
 * @date 2019/8/23  16:27.
 * @Description 非中介表达式公共抽象部分
 */
public abstract class CommonAbstractExpression  extends ArithmeticExpression{

    /**
     * 运算符两边的解释器
     */
    protected ArithmeticExpression exp1,exp2 ;

    public CommonAbstractExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
}
