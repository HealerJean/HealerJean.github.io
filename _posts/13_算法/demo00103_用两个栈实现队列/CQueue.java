package com.hlj.arith.demo00103_用两个栈实现队列;

import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 作者：HealerJean
 题目：用两个栈实现队列
 用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
 解题思路：栈的左右手倒腾
 */
public class CQueue {

    //保证左手一直有值
    private Stack<Integer> left = new Stack<>();
    private Stack<Integer> right = new Stack<>();

    public CQueue() {

    }

    /**
     * 添加到队尾 (保证左手一直有值)
     */
    public void appendTail(int value) {
        left.push(value);
    }

    /**
     * 移除队头 （左右手倒腾）
     */
    public int deleteHead() {
        int val = -1 ;
        while (!left.isEmpty()){
            right.push(left.pop());
            if (left.isEmpty()){
                val =   right.pop();
            }
        }

        //用完再放回去
        while (!right.isEmpty()){
            left.push(right.pop());
        }
        return val;
    }

    @Test
    public void test() {
        CQueue obj = new CQueue();
        obj.appendTail(3);
        obj.appendTail(4);
        obj.appendTail(5);
        obj.appendTail(6);

        System.out.println(obj.deleteHead());
        System.out.println(obj.deleteHead());
        System.out.println(obj.deleteHead());
        System.out.println(obj.deleteHead());
        System.out.println(obj.deleteHead());

    }
}
