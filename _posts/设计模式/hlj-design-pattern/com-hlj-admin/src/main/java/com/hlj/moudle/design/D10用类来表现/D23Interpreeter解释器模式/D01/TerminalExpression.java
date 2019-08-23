package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D01;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TerminalExpression
 * @date 2019/8/23  11:52.
 * @Description 终结符表达式 用来实现文法中与终结符相关的操作，文法中的每一个"终结符"都有一个具体终结表达式与之相对应。
 * 它用集合（Set）类来保存满足条件的城市或人，
 * 并实现抽象表达式接口中的解释方法 interpret(Stringinfo)，用来判断被分析的字符串是否是集合中的终结符。
 */
class TerminalExpression implements AbstractExpression {

    private Set<String> set = new HashSet<>();


    public TerminalExpression(String[] data) {
        set.addAll(  Arrays.stream(data).collect(Collectors.toSet()));
    }

    @Override
    public boolean interpret(String info) {
        if (set.contains(info)) {
            return true;
        }
        return false;
    }
}
