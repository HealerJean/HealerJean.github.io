package com.hlj.arith.demo00012_二叉树遍历;

import org.junit.Test;

public class D3_后序遍历 {

    @Test
    public void test() {
        System.out.println("后序遍历");
        TreeNode node = initTreeNode();
        postOrder(node);
    }

    /**
     *
     * 后续遍历(左右根) ：递归
     */
    public static void postOrder(TreeNode root) {

        if (root.left != null) {
            postOrder(root.left);
        }
        if (root.right != null) {
            postOrder(root.right);
        }

        System.out.println(root.val);
    }




    /**
     * 初始化二叉树：
     * 必须逆序简历，先建立子节点，再逆序往上建立，因为非叶子节点会使用到下面的节点，而初始化是按顺序初始化得，不逆序建立会报错
     */

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
