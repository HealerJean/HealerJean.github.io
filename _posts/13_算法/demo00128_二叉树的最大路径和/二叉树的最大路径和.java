package com.hlj.arith.demo00128_二叉树的最大路径和;


import org.junit.Test;


/**
作者：HealerJean
题目：
给定一个二叉树，找出其路径和
二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
说明: 叶子节点是指没有子节点的节点。
     示例：
         给定二叉树 [3,9,20,null,null,15,7]，
         3
         / \
         9  20
         /  \
         15   7
        返回它的最大路径和 3 + 20 + 7 = 30。
解题思路：
*/
public class 二叉树的最大路径和 {


    @Test
    public void test(){
        System.out.println(treePathMax(initTreeNode()));
    }

    public int treePathMax(TreeNode treeNode) {
        if(treeNode == null){
            return 0;
        }
        int maxLeft = treePathMax(treeNode.left);
        int maxRight = treePathMax(treeNode.right);
        return  Math.max(maxLeft, maxRight) + treeNode.val;
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode1 = new TreeNode(20, null ,null);
        TreeNode treeNode2 = new TreeNode(6, null , null);
        TreeNode treeNode3 = new TreeNode(5, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(5, treeNode3, treeNode4);
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
