package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.D01_添加功能;

import com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.AbstractMachine;
import com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式.CountOperator;

/**
 * @author HealerJean
 * @ClassName TimeOperator
 * @date 2019/8/6  18:15.
 * @Description
 */
public class TimeOperator extends CountOperator {


    public TimeOperator(AbstractMachine machine) {
        super(machine);
    }

    /**时间打印*/
    public void timeTask(Long time){
        try {
            Thread.sleep(time);
            task();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
