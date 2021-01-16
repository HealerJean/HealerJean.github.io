package com.hlj.arith.demo00185_求根到叶子节点数字之和;

import jdk.nashorn.internal.runtime.ListAdapter;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

/**
作者：HealerJean
题目：
 给定一个二叉树，它的每个结点都存放一个 0-9 的数字，每条从根到叶子节点的路径都代表一个数字。
 例如，从根到叶子节点路径 1->2->3 代表数字 123。
 计算从根到叶子节点生成的所有数字之和。
 说明: 叶子节点是指没有子节点的节点。
     示例 1:
         输入: [1,2,3]
              1
             / \
            2   3
         输出: 25
         解释:
             从根到叶子节点路径 1->2 代表数字 12.
             从根到叶子节点路径 1->3 代表数字 13.
             因此，数字总和 = 12 + 13 = 25.
     示例 2:
         输入: [4,9,0,5,1]
             4
             / \
            9   0
          / \
          5   1
         输出: 1026
         解释:
             从根到叶子节点路径 4->9->5 代表数字 495.
             从根到叶子节点路径 4->9->1 代表数字 491.
             从根到叶子节点路径 4->0 代表数字 40.
             因此，数字总和 = 495 + 491 + 40 = 1026.
解题思路：
*/
public class 求根到叶子节点数字之和 {

    @Test
    public void test(){
        System.out.println(sumNumbers1(initTreeNode()));
        System.out.println(sumNumbers(initTreeNode()));
    }


    /**
     * 算法1,找出所有路径，然后变成字符串相加
     */
    public int sumNumbers1(TreeNode root) {
        List<String> res = new ArrayList<>();
        dsf(root, res, "");
        if (!res.isEmpty()) {
            String str = res.get(0);
            for (int i = 1; i < res.size(); i++) {
                str = addStrings(str, res.get(i));
            }
            return Integer.valueOf(str);
        }
        return 0;
    }

    public void dsf(TreeNode node, List<String> res, String str) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            res.add(str + node.val);
            return;
        }
        dsf(node.left, res, str + node.val);
        dsf(node.right, res, str + node.val);
    }

    public String addStrings(String num1, String num2) {
        StringBuilder append = new StringBuilder();

        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int t = 0;
        while (j >= 0 || i >= 0 || t > 0) {
            int pre = i < 0 ? 0 : num1.charAt(i--) - '0';
            int post = j < 0 ? 0 : num2.charAt(j--) - '0';
            int sum = pre + post + t;
            t = sum / 10;
            append.append(sum % 10);
        }
        return append.reverse().toString();
    }


    /**
     * 算法2：官方
     */
    public int sumNumbers(TreeNode root) {
        return dfs(root, 0);
    }

    public int dfs(TreeNode root, int prevSum) {
        if (root == null) {
            return 0;
        }
        int sum = prevSum * 10 + root.val;
        if (root.left == null && root.right == null) {
            return sum;
        }
        int leftNum =   dfs(root.left, sum);
        int rightNum =   dfs(root.right, sum);
        return  leftNum + rightNum;
    }



    public TreeNode initTreeNode(){
        TreeNode treeNode1 = new TreeNode(1, null ,null);
        TreeNode treeNode5 = new TreeNode(5, null , null);
        TreeNode treeNode0 = new TreeNode(0, null, null);
        TreeNode treeNode9 = new TreeNode(9, treeNode5, treeNode1);
        TreeNode root = new TreeNode(4, treeNode9, treeNode0);
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
