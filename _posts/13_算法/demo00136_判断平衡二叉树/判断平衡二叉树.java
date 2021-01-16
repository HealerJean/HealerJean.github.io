package com.hlj.arith.demo00136_判断平衡二叉树;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个二叉树，判断它是否是高度平衡的二叉树。
 本题中，一棵高度平衡二叉树定义为：
 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过1。
 示例 1:
     给定二叉树 [3,9,20,null,null,15,7]
     3
     / \
     9  20
     /  \
     15   7
     返回 true 。
 示例 2:
     给定二叉树 [1,2,2,3,3,null,null,4,4]
     1
     / \
     2   2
     / \
     3   3
     / \
     4   4
     返回 false 。

解题思路：
*/
public class 判断平衡二叉树 {


    @Test
    public void test(){
        System.out.println(isBalanced(initTreeNode()));
    }

    /** 方法1 使用全局变量 */
    boolean flag  = true;
    public boolean isBalanced(TreeNode root) {
        df(root);
        return flag;
    }

    public int df(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = df(root.left);
        int right = df(root.right);
        if (Math.abs(left - right) > 1 ){
            flag = false;
        }
        return Math.max(left, right) + 1;
    }


    /** 方法2  不使用全局变量 （判断高度差以及每棵树都是平衡二叉树）*/
    public boolean isBalanced2(TreeNode root) {
        if (root == null) {
            return true;
        } else {
            return Math.abs(height(root.left) - height(root.right)) <= 1 && isBalanced(root.left) && isBalanced(root.right);
        }
    }

    /** 计算高度差 */
    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            return Math.max(height(root.left), height(root.right)) + 1;
        }
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
