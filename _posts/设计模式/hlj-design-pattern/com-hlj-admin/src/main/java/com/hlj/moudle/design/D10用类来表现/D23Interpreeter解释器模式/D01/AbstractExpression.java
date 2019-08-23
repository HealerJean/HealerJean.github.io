package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D01;

/**
 * @author HealerJean
 * @ClassName AbstractExpression
 * @date 2019/8/23  11:54.
 * @Description   抽象表达式
 * 定义解释器的接口，约定解释器的解释操作，主要包含解释方法 interpret()。
 */
public interface AbstractExpression {

     boolean interpret(String info);
}
