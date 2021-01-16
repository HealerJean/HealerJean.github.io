package com.hlj.arith.demo00055_旋转链表;


import org.junit.Test;

/**
作者：HealerJean
题目：旋转链表
 > 给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。

 **示例 1:**
 ```
 输入: 1->2->3->4->5->NULL, k = 2
 输出: 4->5->1->2->3->NULL
 解释:
 向右旋转 1 步: 5->1->2->3->4->NULL
 向右旋转 2 步: 4->5->1->2->3->NULL
 ```

 **示例 2:**
 ```
 输入: 0->1->2->NULL, k = 4
 输出: 2->0->1->NULL
 解释:
 向右旋转 1 步: 2->0->1->NULL
 向右旋转 2 步: 1->2->0->NULL
 向右旋转 3 步: 0->1->2->NULL
 向右旋转 4 步: 2->0->1->NULL
 ```

 解题思路：
*/
public class 旋转链表 {

    @Test
    public void test(){
        ListNode listNode = listNode();
        printListNode(listNode);

        listNode = rotateRight(listNode, 3);
        printListNode(listNode);
    }

    public ListNode rotateRight(ListNode head, int k) {
        //极端情况
        if (head == null){
            return null;
        }
        if (k == 0){
            return head;
        }

        //节点数
        ListNode listNode = head;
        int nodeCount = 0 ;
        while (head != null) {
            nodeCount++;
            head = head.next;
        }

        //如果k比较大，甚至是head长度的倍数的时候，其实相等于会重复走，所以我们直接取不重复的移动
        if (k == nodeCount){
            return listNode;
        }else if (k > nodeCount){
            k = k % nodeCount;
        }

        //开始真正的 旋转
        return rotateRightMethod(listNode,k);
    }

    public ListNode rotateRightMethod(ListNode head, int k){
        if (k == 0){
            return head;
        }

        //取出第一个节点，因为第一个节点要放到后面去
        ListNode firstNode = head;
        //因为最后一个节点要移动到首个节点，这里我们需要渠道倒数第二个节点，因为倒数第二个节点将会作为最后一个节点
        while (head.next.next != null){
            ListNode next = head.next ;
            head = next;
        }

        //head此时为倒数第二个节点
        head.next.next =  firstNode;
        ListNode node  = head.next;
        head.next = null;

        node =  rotateRightMethod(node, k-1);
        return node;
    }



    void  printListNode(ListNode listNode){
        while (listNode != null){
            System.out.printf( listNode.value + ",");
            listNode = listNode.next;
        }
        System.out.println();
    }


    public ListNode listNode(){
        // ListNode listNode_5 = new ListNode(5, null);
        // ListNode listNode_4 = new ListNode(4, listNode_5);
        // ListNode listNode_3 = new ListNode(2, listNode_4);
        // ListNode listNode_2 = new ListNode(1, listNode_3);
        ListNode listNode_1 = new ListNode(0, null);
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

