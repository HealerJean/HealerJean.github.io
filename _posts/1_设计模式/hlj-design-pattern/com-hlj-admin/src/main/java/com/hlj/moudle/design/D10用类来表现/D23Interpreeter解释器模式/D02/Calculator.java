package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;


/**
 * @author HealerJean
 * @ClassName Calculator
 * @date 2019/8/23  16:19.
 * @Description
 */
public class Calculator {

   private   ArithmeticExpression exp1 ;
    private  ArithmeticExpression exp2 ;

    public Calculator(String expression,String rule){
        String[] elements = expression.split(rule);
        exp1 = new NumExpression(Integer.valueOf(elements[0]));s
        exp2 = new NumExpression(Integer.valueOf(elements[1]));
    }

    public int  add() {
        ArithmeticExpression  add = new AdditionExpression(exp1,exp2);
        return add.interpret();
    }


    public int  sub() {
        ArithmeticExpression  sub = new SubtractionExpression(exp1,exp2);
        return sub.interpret();
    }

}
