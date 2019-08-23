package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D01;

/**
 * @author HealerJean
 * @ClassName D23Main
 * @date 2019/8/23  11:53.
 * @Description
 */
public class D23Main {

    public static void main(String[] args)
    {
        Context bus=new Context();
        bus.freeRide("韶关的老人");
        bus.freeRide("韶关的年轻人");
        bus.freeRide("广州的妇女");
        bus.freeRide("广州的儿童");
        bus.freeRide("山东的儿童");
    }
}
