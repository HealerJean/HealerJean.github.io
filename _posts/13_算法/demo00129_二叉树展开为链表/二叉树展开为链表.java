package com.hlj.arith.demo00129_二叉树展开为链表;


import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.Stack;

/**
作者：HealerJean
题目：二叉树展开为链表
 给定一个二叉树，原地将它展开为一个单链表。
 例如，给定二叉树

 1
 / \
 2   5
 / \   \
 3   4   6
 将其展开为：
 1
  \
   2
    \
     3
      \
       4
        \
         5
          \
          6
解题思路：
*/
public class 二叉树展开为链表 {


    @Test
    public void test(){
        flatten(initTreeNode());
    }

    public void flatten(TreeNode root) {
        while (root != null) {
            if (root.left != null) {
                // 找左子树最右边的节点
                TreeNode pre = root.left;
                while (pre.right != null) {
                    pre = pre.right;
                }

                //将原来的右子树接到左子树的最右边节点，此时 pre.right = null
                pre.right = root.right;
                // 将左子树插入到右子树的地方
                root.right = root.left;
                //此时讲root的左节点设置为null
                root.left = null;
                // 考虑下一个节点
            }
            root = root.right;
        }
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode6 = new TreeNode(6, null, null);
        TreeNode treeNode4 = new TreeNode(4, null, null);
        TreeNode treeNode3 = new TreeNode(3, null, null);
        TreeNode treeNode5 = new TreeNode(5, null ,treeNode6);
        TreeNode treeNode2 = new TreeNode(2, treeNode3 , treeNode4);
        TreeNode root = new TreeNode(1, treeNode2, treeNode5);
        return root ;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
