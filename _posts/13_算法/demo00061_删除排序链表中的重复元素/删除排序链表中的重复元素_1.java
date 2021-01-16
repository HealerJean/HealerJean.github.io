package com.hlj.arith.demo00061_删除排序链表中的重复元素;

import org.junit.Test;

/**
作者：HealerJean
题目：删除排序链表中的重复元素1
 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
 示例 1:
     输入: 1->1->2
     输出: 1->2
 示例 2:
     输入: 1->1->2->3->3
     输出: 1->2->3
解题思路：就是删除中间的指针，没有别的意思
*/
public class 删除排序链表中的重复元素_1 {


    @Test
    public void test(){
        printListNode(deleteDuplicates(initListNode()));
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null){
            return null;
        }

        ListNode node = head ;
        //会比较当前节点和下一个节点的值，所以截止到 倒数第二个节点就好了 （1 2） （1， 1）
        //因为要比较当前节点和下一个节点的值，所以我们要遍历的条件就是下一个节点有值
        while (head.next != null){
             if (head.val == head.next.val){
                 //指针删除了head.next，我们还要继续遍历比较 当前head的值和删除后head.next的值
                 head.next = head.next.next;
             }else {
                 head = head.next;
             }
        }
        return node;
    }



    void  printListNode(ListNode listNode){
        while (listNode != null){
            System.out.printf( listNode.val + ",");
            listNode = listNode.next;
        }
        System.out.println();
    }


    public ListNode initListNode(){
        ListNode listNode_5 = new ListNode(3, null);
        ListNode listNode_4 = new ListNode(3, listNode_5);
        ListNode listNode_3 = new ListNode(1, null);
        ListNode listNode_2 = new ListNode(1, listNode_3);
        ListNode listNode_1 = new ListNode(1, listNode_2);
        return listNode_1;
    }
    class ListNode{
        int val;
        ListNode next ;

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}


