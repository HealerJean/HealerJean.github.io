package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D01;

/**
 * @author HealerJean
 * @ClassName AndExpression
 * @date 2019/8/23  11:55.
 * @Description 非终结符表达式（AndExpressicm）类，用来实现文法中与非终结符相关的操作，文法中的"每条规则"都对应于一个非终结符表达式。
 * 它也是抽象表达式的子类，它包含满足条件的城市的终结符表达式对象和满足条件的人员的终结符表达式对象，
 * 并实现 interpret(String info) 方法，用来判断被分析的字符串是否是满足条件的城市中的满足条件的人员。
 */
public class AndExpression implements AbstractExpression {

    private AbstractExpression city;

    private AbstractExpression person;

    public AndExpression(AbstractExpression city,AbstractExpression person) {
        this.city=city;
        this.person=person;
    }

    /**
     * 校验语法规则
     */
    @Override
    public boolean interpret(String info) {
        String s[] = info.split("的");
        return city.interpret(s[0]) && person.interpret(s[1]);
    }
}
