package com.hlj.arith.demo00085_删除链表的倒数第N个节点;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：HealerJean
 * 题目：删除链表的倒数第N个节点
 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
 说明：给定的 n 保证是有效的。
 进阶：你能尝试使用一趟扫描实现吗？
 示例：
     给定一个链表: 1->2->3->4->5, 和 n = 2.
     当删除了倒数第二个节点后，链表变为 1->2->3->5.
 * 解题思路：
 */
public class 删除链表的倒数第N个节点_1 {

    @Test
    public void test() {
        System.out.println(removeNthFromEnd(listNode(), 2));
    }

    /**
     * 两次遍历
     * 1、第一次遍历求的count的大小
     * 2、第二次找到对应位置，然后删除
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {

        int count = 0;
        ListNode node = head;
        while (head != null) {
            count++;
            head = head.next;
        }
        int diff = count - n;
        if (diff == 0) {
            return node.next;
        }

        ListNode listNode = node;
        while (diff != 0) {
            diff--;
            if (diff == 0) {
                node.next = node.next.next;
            }
            node = node.next;
        }
        return listNode;
    }



    /**
     * 使用Map装入ListNode ,Integer记录List的个数
     */
    public ListNode removeNthFromEnd2(ListNode head, int n) {
        Map<Integer, ListNode> map = new HashMap<>();
        ListNode node = head;
        Integer count = 0 ;
        while (head !=null){
            map.put(++count, head);
            head = head.next;
        }
        //1、如果是第一个则直接返回
        Integer diff = count - n ;
        if (diff == 0){
            return node.next ;
        }

        //2、否则找到要删除的前一位，然后删除即可
        map.get(diff).next = map.get(diff).next.next;
        return node;
    }

    public ListNode listNode() {
        // ListNode listNode_5 = new ListNode(5, null);
        // ListNode listNode_4 = new ListNode(4, listNode_5);
        // ListNode listNode_3 = new ListNode(3, listNode_4);
        ListNode listNode_2 = new ListNode(2, null);
        ListNode listNode_1 = new ListNode(1, listNode_2);
        return listNode_1;
    }

    public String listNodeStr(ListNode listNode, String str) {
        if (listNode == null) {
            return str.substring(0, str.lastIndexOf(","));
        }
        str = str + listNode.val + ",";
        return listNodeStr(listNode.next, str);
    }

    class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
