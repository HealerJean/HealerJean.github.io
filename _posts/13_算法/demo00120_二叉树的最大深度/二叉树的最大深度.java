package com.hlj.arith.demo00120_二叉树的最大深度;


import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
作者：HealerJean
题目：
 给定一个二叉树，找出其最大深度。
 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 说明: 叶子节点是指没有子节点的节点。
     示例：
         给定二叉树 [3,9,20,null,null,15,7]，
         3
         / \
         9  20
         /  \
         15   7
        返回它的最大深度 3 。
解题思路：层序遍历
*/
public class 二叉树的最大深度 {


    @Test
    public void test(){

    }


    /** 递归 想象最底层肯定为0*/
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftMax = maxDepth(root.left);
        int rightMax = maxDepth(root.right);
        return Math.max(leftMax, rightMax) + 1;
    }


    /** 层序遍历 */
    public int maxDepth2(TreeNode root) {
        if (root == null){
            return 0 ;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int max = 0 ;
        while (!queue.isEmpty()){
            int size = queue.size();
            max++;
            while (size > 0 ){
                TreeNode node = queue.remove();
                size--;

                if (node.left != null){
                    queue.add(node.left);
                }
                if (node.right != null){
                    queue.add(node.right);
                }
            }

        }
        return max ;
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
