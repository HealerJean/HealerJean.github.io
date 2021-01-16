package com.hlj.arith.demo00155_左叶子之和;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

/**
作者：HealerJean
题目：计算给定二叉树的所有左叶子之和。
     示例：
         3
         / \
         9  20
         /  \
         15   7
     在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
解题思路：
*/
public class 左叶子之和 {


    @Test
    public void test(){
        System.out.println(sumOfLeftLeaves(initTreeNode()));
    }


    public int sumOfLeftLeaves(TreeNode root) {
        //如果是空的，则返回0，只有首次的时候才回进入
        if (root == null) {
            return 0;
        }

        //做节点的值
        int leftVal = 0;
        //如果左节点不等于空
        if (root.left != null) {
            //如果是叶子节点，则直接获取值即可。否则该节点不是叶子节点，则继续遍历
            if (isLeafNode(root.left)) {
                leftVal = root.left.val;
            } else {
                leftVal = sumOfLeftLeaves(root.left);
            }
        }

        //右节点的值
        int rightVal = 0;
        if (root.right != null && !isLeafNode(root.right)) {
            //只有当不是叶子节点的时候才 会进行回溯
            rightVal = sumOfLeftLeaves(root.right);
        }
        return leftVal + rightVal;
    }

    public boolean isLeafNode(TreeNode node) {
        return node.left == null && node.right == null;
    }



    public TreeNode initTreeNode(){
        // TreeNode treeNode7 = new TreeNode(7, null ,null);
        // TreeNode treeNode15 = new TreeNode(15, null , null);
        // TreeNode treeNode20 = new TreeNode(20, treeNode15, treeNode7);
        // TreeNode treeNode9 = new TreeNode(9, null, null);
        TreeNode root3 = new TreeNode(3, null, null);
        return root3 ;
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
