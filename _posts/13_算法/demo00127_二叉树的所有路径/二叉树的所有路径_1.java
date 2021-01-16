package com.hlj.arith.demo00127_二叉树的所有路径;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 作者：HealerJean
 题目：二叉树的全路径
给定一个二叉树，找出其路径和
     说明: 叶子节点是指没有子节点的节点。
     示例：
         给定二叉树 [3,9,20,null,null,15,7]，
         3
         / \
         9  20
         /  \
         15   7
         返回它的最大路径和 [[3,9,15],[3,20,7]]
 解题思路：
 */
public class 二叉树的所有路径_1 {


    @Test
    public void test(){
        System.out.println(collectTree(initTreeNode()));
    }

    public List<List<Integer>> collectTree(TreeNode treeNode){
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
         dfs(treeNode, res, linkedList);
         return res ;
    }

    public void dfs(TreeNode treeNode, List<List<Integer>> res, LinkedList<Integer> linkedList) {
        if (treeNode == null) {
            return;
        }

        linkedList.add(treeNode.val);
        if (treeNode.right == null || treeNode.left == null) {
            res.add(new ArrayList<>(linkedList));
            return;
        }
        if (treeNode.left != null) {
            dfs(treeNode.left, res, linkedList);
            linkedList.removeLast();
        }
        if (treeNode.right != null) {
            dfs(treeNode.right, res, linkedList);
            linkedList.removeLast();
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
