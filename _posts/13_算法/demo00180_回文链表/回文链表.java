// package com.hlj.arith.demo00180_回文链表;
//
// import org.junit.Test;
//
// import java.util.ArrayList;
// import java.util.List;
//
// /**
// 作者：HealerJean
// 题目：
//  请判断一个链表是否为回文链表。
//  示例 1:
//      输入: 1->2
//      输出: false
//  示例 2:
//      输入: 1->2->2->1
//      输出: true
// 解题思路：
// */
// public class 回文链表 {
//
//     @Test
//     public void test(){
//         System.out.println(isPalindrome(listNode()));
//         System.out.println(isPalindrome2(listNode()));
//
//     }
//
//     /**
//      * 方法1
//      */
//     public boolean isPalindrome(ListNode head) {
//         List<Integer> list = new ArrayList<>();
//         while (head != null){
//             list.add(head.val);
//             head = head.next;
//         }
//         int left = 0 ;
//         int right = list.size() -1 ;
//         while (left < right) {
//             if (list.get(left) .compareTo(list.get(right)) != 0) {
//                 return false;
//             }
//             left++;
//             right--;
//         }
//         return true;
//     }
//
//
//     /**
//      * 2、官方解答
//      */
//     public boolean isPalindrome2(ListNode head) {
//         if (head == null) {
//             return true;
//         }
//
//         // 找到前半部分链表的尾节点并反转后半部分链表
//         ListNode halfEnd = endOfFirstHalf(head);
//         ListNode reverseHalfEnd = reverseList(halfEnd.next);
//
//         // 判断是否回文
//         ListNode p1 = head;
//         ListNode p2 = reverseHalfEnd;
//         boolean result = true;
//         while (result && p2 != null) {
//             if (p1.val != p2.val) {
//                 result = false;
//             }
//             p1 = p1.next;
//             p2 = p2.next;
//         }
//
//         // 还原链表并返回结果
//         halfEnd.next = reverseList(reverseHalfEnd);
//         return result;
//     }
//
//
//     /**
//      * 获取后半部分。使用快慢指针
//      */
//     private ListNode endOfFirstHalf(ListNode head) {
//         ListNode fast = head;
//         ListNode slow = head;
//         // 比如 1 2 1   最后获取的是   1
//         // 比如 1 2 2 1 最后获取的是   2 1
//         while (fast.next != null && fast.next.next != null)
//         return slow;
//     }
//
//
//     /** 链表反转 */
//     private ListNode reverseList(ListNode head) {
//         ListNode prev = null;
//         while (head != null) {
//             ListNode next = head.next;
//             head.next = prev;
//             prev = head;
//             head = next;
//         }
//         return prev;
//     }
//
//
//     public ListNode listNode(){
//         // ListNode listNode_5 = new ListNode(5, null);
//         ListNode listNode_4 = new ListNode(1, null);
//         ListNode listNode_3 = new ListNode(2, listNode_4);
//         ListNode listNode_2 = new ListNode(2, listNode_3);
//         ListNode listNode_1 = new ListNode(1, listNode_2);
//         return listNode_1;
//     }
//
//     class ListNode{
//         int val;
//         ListNode next ;
//         public ListNode(int val) {
//             this.val = val;
//         }
//         public ListNode(int val, ListNode next) {
//             this.val = val;
//             this.next = next;
//         }
//     }
//
// }
