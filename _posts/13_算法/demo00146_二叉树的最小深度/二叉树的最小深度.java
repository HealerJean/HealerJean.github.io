package com.hlj.arith.demo00146_二叉树的最小深度;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个二叉树，找出其最小深度。
 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 说明: 叶子节点是指没有子节点的节点。
 示例:
 给定二叉树 [3,9,20,null,null,15,7],
     3
     / \
     9  20
     /  \
     15   7
     返回它的最小深度  2.

解题思路：
*/
public class 二叉树的最小深度 {


    @Test
    public void test(){
        System.out.println(minDepth(initTreeNode()));
    }

    public int minDepth(TreeNode root) {
        //首次进入判断
        if (root == null) {
            return 0;
        }
        //只有一个节点
        if (root.left == null && root.right == null) {
            return 1;
        }

        //初始化最小的值，后面还有深度，但是不确定哪个
        int min = Integer.MAX_VALUE;
        if (root.left != null) {
            min = Math.min(minDepth(root.left), min);
        }
        if (root.right != null) {
            min = Math.min(minDepth(root.right), min);
        }
        //最后加上自身
        return min + 1;
    }



    public TreeNode initTreeNode(){
       TreeNode treeNode2 = new TreeNode(2, null, null);
       TreeNode root = new TreeNode(1, treeNode2, null);
        return root ;
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
