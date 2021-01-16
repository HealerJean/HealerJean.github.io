package com.hlj.arith.demo00156_把二叉搜索树转换为累加树;


import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个二叉搜索树（Binary Search Tree），把它转换成为累加树（Greater Tree)，使得每个节点的值是原来的节点值加上所有大于它的节点值之和。
     例如：
         输入: 原始二叉搜索树:
             5
             /   \
             2     13
         输出: 转换为累加树:
             18
             /   \
             20     13
解题思路：
 只需要反序中序遍历该二叉搜索树，记录过程中的节点值之和，并不断更新当前遍历到的节点的节点值，即可得到题目要求的累加树。
*/
public class 把二叉搜索树转换为累加树 {

    @Test
    public void test(){
        System.out.println(convertBST(initTreeNode()));
    }


    int sum = 0;
    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            //找到最大的值
            convertBST(root.right);
            sum += root.val;
            root.val = sum;
            //开始走做节点了
            convertBST(root.left);
        }
        return root;
    }

    public TreeNode initTreeNode(){
        TreeNode treeNode1 = new TreeNode(3, null ,null);
        TreeNode treeNode2 = new TreeNode(6, null , null);
        TreeNode treeNode3 = new TreeNode(4, treeNode1, treeNode2);
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
