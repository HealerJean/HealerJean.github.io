package com.hlj.arith.demo00165_二叉树的右视图;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author HealerJean
 * @date 2020/9/29  15:05.
 * @description
 */
public class 二叉树的右视图 {

    @Test
    public void test(){
        System.out.println(rightSideView(initTreeNode()));
    }

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null){
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            int val = 0 ;
            while (size > 0){
                size--;
                TreeNode node = queue.remove();
                val = node.val;

                //先来左节点，最后肯定留下的是右节点
                if (node.left != null){
                    queue.add(node.left);
                }

                if (node.right != null){
                    queue.add(node.right);
                }
            }
            res.add(val);
        }
        return res;
    }


    public TreeNode initTreeNode(){
        TreeNode treeNode5 = new TreeNode(5, null ,null);
        TreeNode treeNode4 = new TreeNode(4, null , null);
        TreeNode treeNode3 = new TreeNode(3, null, treeNode4);
        TreeNode treeNode2 = new TreeNode(2, null, treeNode5);
        TreeNode root1 = new TreeNode(1, treeNode2, treeNode3);
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
