package com.hlj.arith.demo00172_环形链表;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
作者：HealerJean
题目：
 给定一个链表，判断链表中是否有环。
 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
 如果链表中存在环，则返回 true 。 否则，返回 false 。
 进阶：你能用 O(1)（即，常量）内存解决此问题吗？
 示例 1：
     输入：head = [3,2,0,-4], pos = 1
     输出：true
     解释：链表中有一个环，其尾部连接到第二个节点。
 示例 2：
     输入：head = [1,2], pos = 0
     输出：true
     解释：链表中有一个环，其尾部连接到第一个节点。
 示例 3：
     输入：head = [1], pos = -1
     输出：false
     解释：链表中没有环。
 解题思路：
 （有交点，肯定会相遇）
 假想「乌龟」和「兔子」在链表上移动，「兔子」跑得快，「乌龟」跑得慢。当「乌龟」和「兔子」从链表上的同一个节点开始移动时，如果该链表中没有环，那么「兔子」将一直处于「乌龟」的前方；如果该链表中有环，那么「兔子」会先于「乌龟」进入环，并且一直在环内移动。等到「乌龟」进入环时，由于「兔子」的速度快，它一定会在某个时刻与乌龟相遇，即套了「乌龟」若干圈。
*/
public class 环形链表_1 {


    @Test
    public void test(){
        System.out.println(hasCycle(listNode()));
        System.out.println(hasCycle2(listNode()));
    }

    /**
     * 方法1：使用集合不推荐
     */
    public boolean hasCycle(ListNode head) {
        Set<ListNode> seen = new HashSet<>();
        while (head != null) {
            if (!seen.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }


    /**
     * 方法2：推荐
     */
    public boolean hasCycle2(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            //跑的快的先出去
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }


    public ListNode listNode(){
        ListNode listNode_4 = new ListNode(4, null);
        ListNode listNode_0 = new ListNode(0, listNode_4);
        ListNode listNode_2 = new ListNode(2, listNode_0);
        listNode_4.next = listNode_2;
        ListNode listNode_3 = new ListNode(3, listNode_2);
        return listNode_3;
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
