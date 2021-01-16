package com.hlj.arith.demo00137_有序链表转换二叉搜索树;

import com.hlj.arith.z_common.linkNode.ListNodeResources;
import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
     示例:给定的有序链表： [-10, -3, 0, 5, 9], 一个可能的答案是：[0, -3, 9, -10, null, 5], 它可以表示下面这个高度平衡二叉搜索树：
         0
         / \
        -3   9
       /   /
     -10  5
解题思路：先将有序链表转化为有序数组，然后再进行递归
*/
public class 有序链表转换二叉搜索树 {

    @Test
    public void test() {
        sortedListToBST(listNode());
    }

    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        int[] nums = list.stream().mapToInt(Integer::intValue).toArray();
        return inSort(nums, 0, nums.length-1);
    }


    private TreeNode inSort(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        int index = (start + end) / 2;
        TreeNode treeNode = new TreeNode(nums[index]);
        treeNode.left = inSort(nums, start, index - 1);
        treeNode.right = inSort(nums, index + 1, end);
        return treeNode;
    }

    public ListNode listNode() {
        ListNode listNode_5 = new ListNode(9, null);
        ListNode listNode_4 = new ListNode(5, listNode_5);
        ListNode listNode_3 = new ListNode(0, listNode_4);
        ListNode listNode_2 = new ListNode(-3, listNode_3);
        ListNode listNode_1 = new ListNode(-10, listNode_2);
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

    public TreeNode initTreeNode() {
        TreeNode treeNode1 = new TreeNode(3, null, null);
        TreeNode treeNode2 = new TreeNode(6, null, null);
        TreeNode treeNode3 = new TreeNode(4, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(5, treeNode3, treeNode4);
        return root;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;

        }
    }


}
