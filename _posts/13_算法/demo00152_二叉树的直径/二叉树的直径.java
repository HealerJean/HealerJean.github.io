package com.hlj.arith.demo00152_二叉树的直径;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
     示例 :
         给定二叉树
         1
         / \
         2   3
         / \
         4   5
         返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
解题思路：
*/
public class 二叉树的直径 {

    @Test
    public void test(){
        System.out.println(diameterOfBinaryTree(initTreeNode()));
    }

    int res = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        dfs(root);
        return res;
    }

    // 函数dfs的作用是：找到以root为根节点的二叉树的最大深度
    public int dfs(TreeNode root){
        if(root == null){
            return 0;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);
        //获取该节点的最大深度也就是，也就是最大的值，因为肯定是从一个顶点出发的
        res = Math.max(res,  left + right);

        //返回深度
        return Math.max(left,right) + 1;
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
