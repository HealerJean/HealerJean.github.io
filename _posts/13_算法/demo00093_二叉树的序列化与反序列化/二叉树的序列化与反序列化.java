package com.hlj.arith.demo00093_二叉树的序列化与反序列化;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：HealerJean
 * 题目：二叉树的序列化与反序列化
 * 解题思路：
 */
public class 二叉树的序列化与反序列化 {

    @Test
    public void test() {
        System.out.println(serialize(initTreeNode()));
    }

    public String serialize(TreeNode root) {
        StringBuilder str = new StringBuilder();
        mySeri(root, str);
        return str.toString();
    }

    void mySeri(TreeNode root, StringBuilder sb) {
        if (root == null) {
            sb.append("null,");
            return;
        }

        sb.append(root.val + ",");
        mySeri(root.left, sb);
        mySeri(root.right, sb);
    }


    public TreeNode deserialize(String data) {
        List<String> list = new LinkedList<>(Arrays.asList(data.split(",")));
        return myDeSeri(list);
    }

    public TreeNode myDeSeri(List<String> list) {
        TreeNode root;
        String strValue = list.get(0);
        if ("null".equals(strValue)) {
            list.remove(0);
            return null;
        } else {
            int val = Integer.valueOf(strValue);
            root = new TreeNode(val);
            list.remove(0);
            // 当返回null的时候会结束
            root.left = myDeSeri(list);
            root.right = myDeSeri(list);
        }
        return root;
    }


    public TreeNode initTreeNode() {
        TreeNode treeNode4 = new TreeNode(4, null, null);
        TreeNode treeNode3 = new TreeNode(3, null, null);
        TreeNode treeNode2 = new TreeNode(2, treeNode3, treeNode4);

        TreeNode treeNode5 = new TreeNode(5, null, null);
        TreeNode root = new TreeNode(1, treeNode2, treeNode5);
        return root;
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
