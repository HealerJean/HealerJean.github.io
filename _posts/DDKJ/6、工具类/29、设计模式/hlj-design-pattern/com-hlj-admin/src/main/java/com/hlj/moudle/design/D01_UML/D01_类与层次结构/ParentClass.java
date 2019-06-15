package com.hlj.moudle.design.D01_UML.D01_类与层次结构;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ParentClass
 * @date 2019/5/24  11:00.
 * @Description 类图
*/
public abstract class ParentClass {
    int field1;
    static char field2 ;
    abstract void methodA ();
    double methodB(){
        return 0 ;
    }
}

class childClass extends ParentClass {
    void methodA(){
    }
    static void methodC(){
    }
}
