package com.hlj.arith.demo00159_合并二叉树;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

/**
作者：HealerJean
题目：
 给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
 你需要将他们合并为一个新的二叉树。合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，否则不为 NULL 的节点将直接作为新二叉树的节点。
     示例 1:
         输入:
             Tree 1                     Tree 2
             1                         2
             / \                       / \
             3   2                     1   3
             /                           \   \
             5                             4   7
         输出:
             合并后的树:
             3
             / \
             4   5
             / \   \
             5   4   7
 解题思路：
*/
public class 合并二叉树 {



    @Test
    public void test(){
        System.out.println(mergeTrees(initTreeNode(), initTreeNode()));
    }

    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }
        //到了这里说明 都不为空
        TreeNode root = new TreeNode(t1.val + t2.val);

        root.left = mergeTrees(t1.left, t2.left);
        root.right = mergeTrees(t1.right, t2.right);
        return root;
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
