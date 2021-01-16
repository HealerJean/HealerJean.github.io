package com.hlj.arith.demo00162_二叉搜索树的最近公共祖先;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 说明:所有节点的值都是唯一的。p、q 为不同节点且均存在于给定的二叉搜索树中。
 例如，给定如下二叉搜索树:  root = [6,2,8,0,4,7,9,null,null,3,5]
     示例 1:
         输入: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
         输出: 6
         解释: 节点 2 和节点 8 的最近公共祖先是 6。
     示例 2:
         输入: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
         输出: 2
         解释: 节点 2 和节点 4 的最近公共祖先是 2, 因为根据定义最近公共祖先节点可以为节点本身。

解题思路：
*/
public class 二叉搜索树的最近公共祖先 {

    @Test
    public void test(){
        System.out.println(lowestCommonAncestor(initTreeNode(), initTreeNode(), initTreeNode()));
    }


    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }

        // 如果两个节点值都小于根节点，说明他们都在根节点的左子树上，我们往左子树上找
        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor(root.left, p, q);

        // 如果两个节点值都大于根节点，说明他们都在根节点的右子树上，我们往右子树上找
        } else if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor(root.right, p, q);

        // 如果一个节点值大于根节点，一个节点值小于根节点，说明他们他们一个在根节点的左子树上一个在根节点的右子树上，那么根节点就是他们的最近公共祖先节点。
        } else {
            return root;
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
