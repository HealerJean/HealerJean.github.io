package com.hlj.moudle.design.D02_适应者设计模式.D01_Iterator模式.Sample;

/**
 * 表示集合的接口
 * 实现该接口的类将会成一个可以保存多个元素的集合（书架） 、然后书架需要被遍历，则会实现下面的方法
 */
public interface Aggregate {
     Iterator iterator();
}
