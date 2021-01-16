package com.hlj.arith.demo00127_二叉树的所有路径;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个二叉树，返回所有从根节点到叶子节点的路径。
 说明: 叶子节点是指没有子节点的节点。
 示例:
     输入:
     1
     /   \
     2     3
     \
     5
     输出: ["1->2->5", "1->3"]
     解释: 所有根节点到叶子节点的路径为: 1->2->5, 1->3
解题思路：
*/
public class 二叉树的所有路径_2 {


    @Test
    public void test(){
        System.out.println(binaryTreePaths(initTreeNode()));
    }

    public List<String> binaryTreePaths(TreeNode root) {

        List<String> res = new ArrayList<>();
        dfs(root, res, "");
        return res ;
    }

    /** 所有的都会走一遍 有点类似于路径总和1 */
    public void dfs(TreeNode root, List<String> res , String path) {
        if (root == null){
            return;
        }
        if (root.left == null && root.right == null){
            res.add(path + root.val);
            return;
        }

        //如果不是叶子节点，在分别遍历他的左右子节点
        dfs(root.left, res, path + root.val + "->");
        dfs(root.right, res, path + root.val + "->");
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
