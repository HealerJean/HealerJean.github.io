package com.hlj.arith.demo00151_翻转二叉树;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

/**
作者：HealerJean
题目：
 翻转一棵二叉树。

 示例：
     输入：
           4
         /   \
        2     7
       / \   / \
      1   3 6   9
     输出：
           4
         /   \
         7     2
        / \   / \
       9   6 3   1
解题思路：
*/
public class 翻转二叉树 {


    @Test
    public void test(){
        System.out.println(invertTree(initTreeNode()));
    }

    /**
     * 1、我的解法
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null){
            return null;
        }
        //左右节点交换
        TreeNode temp = root.left ;
        root.left = root.right;
        root.right = temp;

        invertTree(root.left);
        invertTree(root.right);

        return root;
    }



    /**
     * 2、官方解法
     */
    public TreeNode invertTree2(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree2(root.left);
        TreeNode right = invertTree2(root.right);

        root.left = right;
        root.right = left;
        return root;
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode6 = new TreeNode(6, null, null);
        TreeNode treeNode9 = new TreeNode(9, null, null);
        TreeNode treeNode7 = new TreeNode(7, treeNode6, treeNode9);


        TreeNode treeNode3 = new TreeNode(3, null, null);
        TreeNode treeNode1 = new TreeNode(1, null ,null);
        TreeNode treeNode2 = new TreeNode(2, treeNode1 , treeNode3);
        TreeNode root4 = new TreeNode(4, treeNode2, treeNode7);
        return root4 ;
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
