package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D02;

/**
 * @author HealerJean
 * @ClassName D02Main
 * @date 2019/8/23  16:30.
 * @Description
 */
public class D02Main {

    public static void main(String[] args) {

        Calculator add = new Calculator("1+2","\\+");
        System.out.println("1+2 = " + add.add());

        Calculator sub = new Calculator("1-2","\\-");
        System.out.println("1-2 = " + sub.sub());
    }
}
