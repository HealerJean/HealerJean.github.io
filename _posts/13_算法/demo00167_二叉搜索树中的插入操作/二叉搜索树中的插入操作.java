package com.hlj.arith.demo00167_二叉搜索树中的插入操作;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定二叉搜索树（BST）的根节点和要插入树中的值，将值插入二叉搜索树。 返回插入后二叉搜索树的根节点。 输入数据保证，新值和原始二叉搜索树中的任意节点值都不同。
 注意，可能存在多种有效的插入方式，只要树在插入后仍保持为二叉搜索树即可。 你可以返回任意有效的结果。
 例如,
 给定二叉搜索树:
 4
 / \
 2   7
 / \
 1   3
 和 插入的值: 5
 解题思路：
*/
public class 二叉搜索树中的插入操作 {


    @Test
    public void test(){
        System.out.println(insertIntoBST(initTreeNode(), 5));
    }


    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        TreeNode curTreeNode = root;
        while (curTreeNode != null) {
            if (curTreeNode.val < val) {
                if (curTreeNode.right == null) {
                    curTreeNode.right = new TreeNode(val);
                    break;
                } else {
                    curTreeNode = curTreeNode.right;
                }
            } else {
                if (curTreeNode.left == null) {
                    curTreeNode.left = new TreeNode(val);
                    break;
                } else {
                    curTreeNode = curTreeNode.left;
                }
            }
        }
        return root;
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode1 = new TreeNode(3, null ,null);
        TreeNode treeNode2 = new TreeNode(1, null , null);
        TreeNode treeNode3 = new TreeNode(7, null, null);
        TreeNode treeNode4 = new TreeNode(2, treeNode2, treeNode1);
        TreeNode root = new TreeNode(4, treeNode4, treeNode3);
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
