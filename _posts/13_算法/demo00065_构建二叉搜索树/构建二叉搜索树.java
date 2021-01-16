package com.hlj.arith.demo00065_构建二叉搜索树;


import com.hlj.arith.demo00012_二叉树遍历.D1_先序遍历;
import org.junit.Test;
import sun.reflect.generics.tree.Tree;

/**
 * @author HealerJean
 * @ClassName 构建二叉搜索树
 * @date 2020/5/7  17:39.
 * @Description
 */
public class 构建二叉搜索树 {


    @Test
    public void test(){
        // 树根节点
        int[] array = new int[]{1,2,4,3};

        preOrder(create(array));
    }


    /**
     * 构建一颗普通的二叉树（假设二叉树不存在相等的情况）
     */
    public TreeNode create(int[] array) {
        //待插入的节点
        TreeNode root = new TreeNode(array[0]);
        for (int i = 1 ; i < array.length ; i++){
            TreeNode curNode = root;
            while (curNode != null){
                //Tree
                if (array[i] >  curNode.val){
                    if (curNode.right == null){
                        curNode.right = new TreeNode(array[i]);
                        break;
                    }else {
                        curNode = curNode.right ;
                    }
                }else {
                    if (curNode.left == null){
                        curNode.left = new TreeNode(array[i]);
                        break;
                    }else {
                        curNode = curNode.left ;
                    }
                }
            }
        }
        return root ;
    }


    /**
     * 删除节点
     */
    public void deleteTreeNode(TreeNode root ,int x){


    }


    /**
     * 先序遍历（根左右）：递归
     */
    public  void preOrder(TreeNode root) {

        //1、打印根节点
        System.out.println(root.val + ",");

        //2、使用递归遍历左孩子
        if (root.left != null) {
            preOrder(root.left);
        }

        //3、使用递归遍历右孩子
        if (root.right != null) {
            preOrder(root.right);
        }
    }


public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}


}
