package com.hlj.arith.demo00177_重排链表;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个单链表 L：L0→L1→…→Ln-1→Ln ，将其重新排列后变为： L0→Ln→L1→Ln-1→L2→Ln-2→…你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
     示例 1:
        给定链表 1->2->3->4, 重新排列为 1->4->2->3.
     示例 2:
         给定链表 1->2->3->4->5, 重新排列为 1->5->2->4->3.
解题思路：
*/
public class 重排链表 {

    @Test
    public void test(){
        reorderList(listNode());
    }

    public void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode node = head;
        //放到List集合中
        List<ListNode> list = new ArrayList<>();
        while (node != null) {
            list.add(node);
            node = node.next;
        }

        int left = 0;
        int right = list.size() - 1;
        while (left < right) {
            ListNode leftNode = list.get(left);
            ListNode rightNode = list.get(right);

            leftNode.next = rightNode;
            rightNode.next = list.get(left + 1);

            left++;
            right--;
        }

        //遍历结束最后一个节点的next肯定是null。以left为主
        list.get(left).next = null;
    }



    public ListNode listNode(){
        // ListNode listNode_5 = new ListNode(5, null);
        ListNode listNode_4 = new ListNode(4, null);
        ListNode listNode_3 = new ListNode(3, listNode_4);
        ListNode listNode_2 = new ListNode(2, listNode_3);
        ListNode listNode_1 = new ListNode(1, listNode_2);
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
