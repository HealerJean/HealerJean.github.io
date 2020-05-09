package com.hlj.moudle.design.D02_交给子类.D03_TeampleMethod模式.TemplateMethod;

/**
 * @author HealerJean
 * @ClassName AbstracatUserService
 * @date 2020/5/9  15:44.
 * @Description
 */
public abstract class AbstracatUserService {

     public void addUser(){
         System.out.println("添加用户");
     }

    abstract void voidCheckUser();

}
