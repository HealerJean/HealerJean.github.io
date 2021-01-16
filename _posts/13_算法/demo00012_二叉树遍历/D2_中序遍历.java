package com.hlj.arith.demo00012_二叉树遍历;

import com.hlj.arith.z_common.TestMain;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class D2_中序遍历 {

    @Test
    public void test() {
        System.out.println("中序遍历");
        TreeNode node = initTreeNode();
        inOrder(node);
        System.out.println();
        inStack(node);
    }

    /**
     *
     * 中序遍历（左根右） ：递归
     */
    public void inOrder(TreeNode root) {
        //使用递归遍历左孩子
        if (root.left != null) {
            inOrder(root.left);
        }
        //打印根节点
        System.out.println(root.val);

        //使用递归遍历右孩子
        if (root.right != null) {
            inOrder(root.right);
        }
    }

    /**
     * 中序遍历（左根右） ：非递归
     * 栈的特性：后进先出，先进后出
     * 1、因为初次打印就是左节点，所以我们需要讲右面的节点全部入栈，知道左子树为空
     * 2、这个时候栈里面有数据了，出栈，直接打印根节点，然后获取右子树。
     * ​		如果右子树不为空，讲当前节点设置为  则执行1，继续获取所有的节点，并入栈
     * ​		如果右子树为空， 则继续执行2，打印节点即可
     */
    public void inStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                //后进先出，讲所有的左子树和跟节点依次放入
                stack.push(node);
                node = node.left;
            }
            //消化队列中的数据
            if (!stack.isEmpty()) {
                node = stack.pop();
                System.out.println(node.left);
                node = node.right;
            }
        }
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
