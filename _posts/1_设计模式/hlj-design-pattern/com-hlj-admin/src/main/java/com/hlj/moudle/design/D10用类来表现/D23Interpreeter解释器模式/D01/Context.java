package com.hlj.moudle.design.D10用类来表现.D23Interpreeter解释器模式.D01;

/**
 * @author HealerJean
 * @ClassName Context
 * @date 2019/8/23  11:53.
 * @Description ：通常包含各个解释器需要的数据
 * 它包含解释器需要的数据，完成对终结符表达式的初始化，并定义一个方法 freeRide(String info) 调用表达式对象的解释方法来对被分析的字符串进行解释。其结构图如图 3 所示。
 */
public class Context {


    private String[] citys = {"韶关", "广州"};
    private String[] persons = {"老人", "妇女", "儿童"};

    private AbstractExpression cityPerson;

    public Context() {
        //初始化终结符表达式
        AbstractExpression city = new TerminalExpression(citys);
        AbstractExpression person = new TerminalExpression(persons);
        cityPerson = new AndExpression(city, person);
    }

    public void freeRide(String info) {
        boolean ok = cityPerson.interpret(info);
        if (ok) {
            System.out.println("您是" + info + "，您本次乘车免费！");
        } else {
            System.out.println(info + "，您不是免费人员，本次乘车扣费2元！");
        }
    }
}
