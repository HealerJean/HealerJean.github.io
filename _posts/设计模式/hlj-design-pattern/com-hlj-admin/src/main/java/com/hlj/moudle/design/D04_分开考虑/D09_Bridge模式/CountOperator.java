package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式;

/**
 * 数字操作员
 */
public class CountOperator extends Operator {

    public CountOperator(AbstractMachine machine) {
        super(machine);
    }

    /**
     * 机器执行多少次后关闭
     */
    public void multiDisplay(int times) {
        for (int i = 0; i < times; i++) {
            System.out.println("第"+i+"次运行");
            open();
            run();
            close();
        }
    }

}
