package com.hlj.arith.demo00161_两数相加;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     输出：7 -> 0 -> 8
     原因：342 + 465 = 807
解题思路：
*/
public class 两数相加 {

    @Test
    public void test(){
        System.out.println(addTwoNumbers(initListNode(), initListNode()));
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0);
        ListNode preNode = root;
        int carry = 0;
        while (l1 != null || l2 != null || carry > 0) {
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;

            preNode.next = new ListNode(sum % 10);
            preNode = preNode.next;

            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        return root.next;
    }



    public ListNode initListNode(){
        ListNode listNode_9 = new ListNode(9, null);
        ListNode listNode_8 = new ListNode(9, listNode_9);
        ListNode listNode_7 = new ListNode(9, listNode_8);
        ListNode listNode_6 = new ListNode(9, listNode_7);
        ListNode listNode_5 = new ListNode(9, listNode_6);
        ListNode listNode_4 = new ListNode(9, listNode_5);
        ListNode listNode_3 = new ListNode(9, listNode_4);
        ListNode listNode_2 = new ListNode(9, listNode_3);
        ListNode listNode_1 = new ListNode(1, listNode_2);
        return listNode_1;
    }

    public String listNodeStr(ListNode listNode, String str){
        if (listNode == null){
            return str.substring(0, str.lastIndexOf(","));
        }
        str = str + listNode.val + "," ;
        return listNodeStr(listNode.next, str);
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
