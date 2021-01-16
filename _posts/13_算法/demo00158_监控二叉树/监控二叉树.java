package com.hlj.arith.demo00158_监控二叉树;

import org.junit.Test;

/**
作者：HealerJean
题目：监控二叉树
 给定一个二叉树，我们在树的节点上安装摄像头。节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。计算监控树的所有节点所需的最小摄像头数量。
 解题思路：
     分析：
         如果  root 处安放摄像头，则孩子 left,right 一定也会被监控到。此时，只需要保证left 和right 的两棵子树都被覆盖即可。
         如果  root 处不安放摄像头，则除了root两棵子树需要被覆盖之外，孩子 left,right 之一必须要安装摄像头，从而保证root 会被监控到。
    讨论：因此本层的状态是由下一层的状态而决定的，而状态根据这样来分析得到的：一个摄像头最多影响3层（本层、下一层、上一层）；
      1、  下一层如果能被观测到但没有摄像头，那么本层肯定是不被观测得到的（0->1）；
      2、  如果下一层安装了摄像头，本层能够被观测到（2->0）；
      3、  下一层如果不能被观测得到，那么本层一定安装了摄像头（1->2）。 ps：括号内容为 下一层状态为a 决定 本层状态为b
    实战：0=>这个结点待覆盖，1=>这个结点已经覆盖， 2=>这个结点上安装了相机

 */
public class 监控二叉树 {

    @Test
    public void test(){
        System.out.println(minCameraCover(initTreeNode()));
    }


    /**
     *  0=>没有覆盖，这个结点待覆盖，1=>这个结点已经覆盖， 2=>这个结点上安装了相机
     */
    int count = 0;
    public int minCameraCover(TreeNode root) {
        //如果当前结点没有被覆盖的话，说明当前结点要安装一个摄像头
        if (dfs(root) == 0) {
            count++;
        }
        return count;
    }


    /**
     * 本层的状态是由下一层的状态而决定的，0=>没有覆盖，这个结点待覆盖，1=>这个结点已经覆盖， 2=>这个结点上安装了相机
     */
    public int dfs(TreeNode root) {
        //空节点设为已覆盖（空的肯定不用搭理）
        if (root == null) {
            return 1;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);

        //左右两个节点都没有覆盖，说明当前结点要安装摄像头
        if (left == 0 || right == 0) {
            count++;
            return 2;

            // 子节点均被监视，则当前节点需要被覆盖，返回待覆盖0（本层状态由下一层决定，下一层都监视了。不管 子集）
        } else if (left == 1 && right == 1) {
            return 0;
         // left + right >= 3 //子节点至少有一个安装了摄像头，则当前节点被覆盖了
         // 1、left + right  = 4 子节点都是已安装摄像头
         // 2、 left + right = 3 子节点至少有一个摄像头
        } else {
            return 1;
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
