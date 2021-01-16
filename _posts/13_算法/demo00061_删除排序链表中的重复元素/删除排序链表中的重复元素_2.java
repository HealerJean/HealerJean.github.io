package com.hlj.arith.demo00061_删除排序链表中的重复元素;

import org.junit.Test;

/**
作者：HealerJean
题目：删除排序链表中的重复元素1
 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
     示例 1:
         输入: 1->2->3->3->4->4->5
         输出: 1->2->5
     示例 2:
         输入: 1->1->1->2->3
         输出: 2->3
解题思路：就是删除中间的指针，没有别的意思
*/
public class 删除排序链表中的重复元素_2 {


    @Test
    public void test(){
        printListNode(deleteDuplicates(initListNode()));
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null){
            return null;
        }
        //如果只有1个节点，则直接返回就行了。肯定不会重复的
        if (head.next == null){
            return head ;
        }


        //新节点头
        ListNode root = new ListNode(-1, null) ;
        ListNode lastNode = root ;


        //初始化比较的值
        int val = head.val ;
        int count = 1 ;
        while (head.next != null){
             if (val == head.next.val){
                 head.next = head.next.next;
                 count ++ ;
             }else {
                 val = head.next.val;
                 ListNode next = head.next;

                 if (count > 1){
                     count = 1 ;
                 }else {
                     lastNode.next = head;
                     //必须有这行
                     lastNode.next.next = null;
                     lastNode = lastNode.next;
                 }
                 head =  next ;
             }
        }

        //上面判断 是 head.next 会存在尾节点有值不走的情况
        if (count == 1){
            lastNode.next = head;
        }
        return root.next;
    }


    void  printListNode(ListNode listNode){
        while (listNode != null){
            System.out.printf( listNode.val + ",");
            listNode = listNode.next;
        }
        System.out.println();
    }


    public ListNode initListNode(){
        // ListNode listNode_5 = new ListNode(4, null);
        // ListNode listNode_4 = new ListNode(4, null);
        // ListNode listNode_3 = new ListNode(3, listNode_4);
        // ListNode listNode_2 = new ListNode(2, listNode_3);
        // ListNode listNode_1 = new ListNode(1, listNode_2);


        ListNode listNode_3 = new ListNode(2, null);
        ListNode listNode_2 = new ListNode(2, listNode_3);
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
