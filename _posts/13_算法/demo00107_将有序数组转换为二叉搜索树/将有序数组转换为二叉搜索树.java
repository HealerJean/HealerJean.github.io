package com.hlj.arith.demo00107_将有序数组转换为二叉搜索树;


import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
作者：HealerJean
题目：
 将一个按照升序排列的有序数组，转换为一棵高度平衡二叉搜索树。
 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
     示例:
         给定有序数组: [-10,-3,0,5,9],
         一个可能的答案是：[0,-3,9,-10,null,5]，它可以表示下面这个高度平衡二叉搜索树：
 0
 / \
 -3   9
 /   /
 -10  5
解题思路： 根据中序遍历的特性，总是寻找 中间的节点作为跟节点
*/
public class 将有序数组转换为二叉搜索树 {


    @Test
    public void test(){
        int[] nums = {-10, -3, 0, 5, 9};
        TreeNode treeNode = sortedArrayToBST(nums);
        LinkedList<Integer> linkedList = new LinkedList<>();
        collect(treeNode, linkedList);
        System.out.println(linkedList);
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        return inSort(nums, 0 , nums.length-1);
    }

    public TreeNode inSort(int[] nums, int strat, int end) {
        //边界判断
        if (strat > end) {
            return null;
        }
        //找出排序数组的中间位置
        int index = (strat + end)/2 ;
        TreeNode node = new TreeNode(nums[index]) ;
        node.left = inSort(nums, strat, index-1);
        node.right = inSort(nums, index+ 1, end);
        return node;
    }







    public  void collect(TreeNode root, LinkedList<Integer> linkedList) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            //表示每行有多少个
            int hangSize = queue.size();
            //遍历每行的数据
            while (hangSize > 0) {
                //从队列中取出，打印根节点
                TreeNode node  = queue.remove();
                hangSize--;
                if (node == null){
                    linkedList.add(null);
                }else {
                    linkedList.add(node.val);
                    queue.add(node.left);
                    queue.add(node.right);
                }
            }
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
