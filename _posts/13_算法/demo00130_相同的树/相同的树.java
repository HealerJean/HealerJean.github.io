package com.hlj.arith.demo00130_相同的树;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

/**
作者：HealerJean
题目：
 给定两个二叉树，编写一个函数来检验它们是否相同。
 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
     示例 1:
         输入:       1         1
         / \       / \
         2   3     2   3
         [1,2,3],   [1,2,3]
         输出: true
     示例 2:
         输入:      1          1
         /           \
         2             2
         [1,2],     [1,null,2]
         输出: false
     示例 3:
         输入:       1         1
         / \       / \
         2   1     1   2
         [1,2,1],   [1,1,2]
         输出: false

解题思路：
*/
public class 相同的树 {


    @Test
    public void test() {
        System.out.println(isSameTree(null, initTreeNode2()));
    }

    /** 深度优先搜索和 先序遍历的结合 */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if ((p == null && q == null)) {
            return true;
        } else if (p == null && q != null) {
            return false;
        } else if (q == null && p != null) {
            return false;
        } else if (p.val != q.val) {
            return false;
        }
        //到了这里说明 p.val == q.val
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }


    public TreeNode initTreeNode() {
        TreeNode treeNode1 = new TreeNode(3, null, null);
        TreeNode treeNode2 = new TreeNode(6, null, null);
        TreeNode treeNode3 = new TreeNode(4, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(5, treeNode3, treeNode4);
        return root;
    }


    public TreeNode initTreeNode2() {
        TreeNode treeNode1 = new TreeNode(3, null, null);
        TreeNode treeNode2 = new TreeNode(6, null, null);
        TreeNode treeNode3 = new TreeNode(4, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(5, treeNode3, treeNode4);
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
