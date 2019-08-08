package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.D02添加实现;

import com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.AbstractMachine;

/**
 * @author HealerJean
 * @ClassName NewMachine
 * @date 2019/8/6  18:03.
 * @Description 机器维护
 */
public class NewMachine extends AbstractMachine {

    private  String string ;

    public NewMachine(String string) {
        this.string = string ;
    }
    @Override
    public void open() {
        System.out.println("新机器 open");
    }

    @Override
    public void run() {
        System.out.println("新机器 run");

    }

    @Override
    public void close() {
        System.out.println("新机器 close");
    }


    public void repair(){
        System.out.println(string+"开始修理");
    }
}
