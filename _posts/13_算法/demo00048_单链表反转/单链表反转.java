package com.hlj.arith.demo00048_单链表反转;

import org.junit.Test;

/**
作者：HealerJean
题目：单链表反转
解题思路：
*/
public class 单链表反转 {


    void  printListNode(ListNode listNode){
        while (listNode != null){
            System.out.printf( listNode.value + ",");
            listNode = listNode.next;
        }
        System.out.println();
    }

    @Test
    public void test(){
        ListNode listNode = listNode();
        ListNode newListNode =  reverseList(listNode);
        // printListNode(newListNode);

        // ListNode newListNode = dgReverseList(listNode);
        printListNode(newListNode);
    }

    public  ListNode reverseList(ListNode head) {
        // 定义新链表头结点
        ListNode reHead = null;
        while (head != null) {
            // 先取出，下一个节点。（后面要进行遍历，提前取出防止发生变化）
            ListNode next = head.next;

            // 将rehead节点怼到head节点上
            head.next = reHead;
            // 再让head节点作为新节点的头
            reHead = head;

            // 将head指向下一个节点进行遍历
            head = next;
        }
        return reHead;
    }


    /**
     * 递归实现
     */
    public static ListNode dgReverseList(ListNode head) {
        if (head.next == null) {
            return head;
        }
        ListNode newHead = dgReverseList(head.next);
        // 将头节点置于末端 （比如将4 - > 5 -> next 设置为 4）
        head.next.next = head;
        // 类似于断开连接，等待下次别人给值 （比如将：4 ->next 设置为 null，等待3到的时候，给值 ）
        head.next = null;
        return newHead;
    }


    public ListNode listNode(){
        ListNode listNode_5 = new ListNode(5, null);
        ListNode listNode_4 = new ListNode(4, listNode_5);
        ListNode listNode_3 = new ListNode(3, listNode_4);
        ListNode listNode_2 = new ListNode(2, listNode_3);
        ListNode listNode_1 = new ListNode(1, listNode_2);
        return listNode_1;
    }

    class ListNode{
        int value ;
        ListNode next ;

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }
}



