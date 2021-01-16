package com.hlj.arith.demo00168_二叉搜索树的最小绝对差;

import org.junit.Test;

/**
作者：HealerJean
题目：二叉搜索树的最小绝对差
 给你一棵所有节点为非负值的二叉搜索树，请你计算树中任意两节点的差的绝对值的最小值。
     示例：
         输入：
             1
              \
               3
                /
                 2
         输出： 1
         解释：  最小绝对差为 1，其中 2 和 1 的差的绝对值为 1（或者 2 和 3）。
解题思路：中序遍历
*/
public class 二叉搜索树的最小绝对差 {

    @Test
    public void test(){
        System.out.println(getMinimumDifference(initTreeNode()));
    }

    int pre = -1;
    int res = Integer.MAX_VALUE;
    public int getMinimumDifference(TreeNode root) {
        dfs(root);
        return res;
    }

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        if (pre == -1) {
            pre = root.val;
        } else {
            res = Math.min(res, root.val - pre);
            pre = root.val;
        }
        dfs(root.right);
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
