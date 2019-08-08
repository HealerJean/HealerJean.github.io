package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式;

import com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.D01_添加功能.TimeOperator;
import com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.D02添加实现.NewMachine;

public class Main {

    public static void main(String[] args) {

        /** 操作员 */
        Operator one = new Operator(new StringMachine("机器ONE"));
        one.task();
        System.out.println("---------------------");

        Operator two = new CountOperator(new StringMachine("机器TWO"));
        two.task();
        System.out.println("---------------------");

        CountOperator three = new CountOperator(new StringMachine("机器TTTTT"));
        three.task();
        System.out.println("---------------------");

        three.multiDisplay(2);

        //
        // 机器ONE开启
        // 机器ONE运行
        // 机器ONE关闭
        // ---------------------
        // 机器TWO开启
        // 机器TWO运行
        // 机器TWO关闭
        // ---------------------
        // 机器TTTTT开启
        // 机器TTTTT运行
        // 机器TTTTT关闭
        // ---------------------
        // 第0次运行
        // 机器TTTTT开启
        // 机器TTTTT运行
        // 机器TTTTT关闭
        // 第1次运行
        // 机器TTTTT开启
        // 机器TTTTT运行
        // 机器TTTTT关闭


        Operator operator = new CountOperator(new NewMachine("新加的机器"));
        operator.task();
        System.out.println("---------------------");
        // 新机器 open
        // 新机器 run
        // 新机器 close



        TimeOperator timeOperator = new TimeOperator(new NewMachine("新加的机器"));
        System.out.println("-------延迟50毫秒执行--------------");
        timeOperator.timeTask(500L);





    }
}
