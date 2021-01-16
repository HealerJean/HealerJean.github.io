package com.hlj.arith.demo00169_两两交换链表中的节点;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
     示例 1：
         输入：head = [1,2,3,4]
         输出：[2,1,4,3]
     示例 2：
         输入：head = []
         输出：[]
     示例 3：
         输入：head = [1]
         输出：[1]
 解题思路：
*/
public class 两两交换链表中的节点 {


    @Test
    public void test(){
        System.out.println(swapPairs(listNode()));
    }

    public ListNode swapPairs(ListNode head) {
        ListNode listNode = new ListNode(-1);
        ListNode preNode  = listNode;
        while (head != null){
            //后面如果只有一个节点则结束
            if (head.next == null){
                preNode.next = head;
                break;
            }
            //提前第三个节点
            ListNode lastNode = head.next.next;
            preNode.next = head.next;
            preNode.next.next = head;
            //防止死循环，复制为null
            preNode.next.next.next = null;

            //将第二个节点赋值给开头
            preNode = preNode.next.next;
            head = lastNode;
        }
        return listNode.next;
    }



    public ListNode listNode(){
        ListNode listNode_4 = new ListNode(4, null);
        ListNode listNode_3 = new ListNode(3, listNode_4);
        ListNode listNode_2 = new ListNode(2, listNode_3);
        ListNode listNode_1 = new ListNode(1, listNode_2);

        // ListNode listNode_1 = new ListNode(1, null);

        return listNode_1;
    }

    class ListNode{
        int val;
        ListNode next ;
        public ListNode(int val) {
            this.val = val;
        }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
