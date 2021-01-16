package com.hlj.arith.demo00069_路径总和;

import org.junit.Test;

/**
作者：HealerJean
题目：路径总和1
 给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和。
 说明: 叶子节点是指没有子节点的节点。
     示例: 
         给定如下二叉树，以及目标和 sum = 22，
         5
         / \
         4   8
         /   / \
         11  13  4
         /  \      \
         7    2      1
         返回 true, 因为存在目标和为 22 的根节点到叶子节点的路径 5->4->11->2。
解题思路：路径总和1
*/
public class 路径总和1 {

    @Test
    public void test(){
        TreeNode treeNode = initTreeNode();
        System.out.println(hasPathSum(treeNode, 22));
    }


    public boolean hasPathSum(TreeNode root, int sum) {
        //因为后面是或者关系，所以只要有一个为true就可以了
        if (root == null) {
            return false;
        }
        if (root.val == sum && root.left ==null && root.right == null) {
            return true;
        }
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }



    public TreeNode initTreeNode(){
        TreeNode treeNode1 = new TreeNode(7, null ,null);
        TreeNode treeNode2 = new TreeNode(2, null , null);
        TreeNode treeNode3 = new TreeNode(11, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode treeNode5 = new TreeNode(4, null, treeNode4);
        TreeNode treeNode6 = new TreeNode(13, null, null);
        TreeNode treeNode7 = new TreeNode(8, treeNode6, treeNode5);
        TreeNode treeNode8 = new TreeNode(4, treeNode3, null);
        TreeNode root = new TreeNode(5, treeNode8, treeNode7);
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

