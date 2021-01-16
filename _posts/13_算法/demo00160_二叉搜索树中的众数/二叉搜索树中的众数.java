package com.hlj.arith.demo00160_二叉搜索树中的众数;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 给定一个有相同值的二叉搜索树（BST），找出 BST 中的所有众数（出现频率最高的元素）。
 假定 BST 有如下定义：
     1、结点左子树中所含结点的值小于等于当前结点的值
     2、结点右子树中所含结点的值大于等于当前结点的值
     3、左子树和右子树都是二叉搜索树
 例如：
    给定 BST [1,null,2,2],
 例如：
     给定 BST [1,null,2,2],
     1
     \
     2
     /
     2
     返回[2].
     提示：如果众数超过1个，不需考虑输出顺序
解题思路：
*/
public class 二叉搜索树中的众数 {


    @Test
    public void test(){
        System.out.println(Arrays.toString(findMode(initTreeNode())));
    }

    Set<Integer> set = new HashSet<>();
    int cur = 0 ;
    int curLen = 0;
    int maxLen = 0;
    boolean firstFlag = true;
    public int[] findMode(TreeNode root) {
        if (root == null){
            return new int[0];
        }
        inOrder(root);
        return set.stream().mapToInt(Integer::intValue).toArray();
    }


    public void inOrder(TreeNode root) {
        if (root.left != null) {
            inOrder(root.left);
        }
        //首次进入初始化值
        if (firstFlag) {
            cur = root.val;
            set.add(cur);
            curLen = 1;
            maxLen = 1;
            firstFlag = false;
        } else {
            //如果相等的话，开始加
            if (cur == root.val) {
                curLen++;
                //如果和最大的相等了，则也放进去
                if (curLen == maxLen) {
                    set.add(cur);
                //如果比最大的要长，则清空set，重新放入并更新最大的长度
                } else if (curLen > maxLen) {
                    set.clear();
                    set.add(cur);
                    maxLen = curLen;
                }
             //如果不相等的话，则进行下一个
            } else {
                cur = root.val;
                curLen = 1;
                //第一个就相同则进入
                if (curLen == maxLen) {
                    set.add(cur);
                }
            }
        }


        if (root.right != null) {
            inOrder(root.right);
        }
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode4 = new TreeNode(2, null, null);
        TreeNode treeNode3 = new TreeNode(1, null, null);
        TreeNode root1 = new TreeNode(1, treeNode3, treeNode4);
        return root1 ;
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
