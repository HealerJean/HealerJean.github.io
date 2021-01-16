package com.hlj.arith.demo00012_二叉树遍历;

import lombok.Data;
import org.junit.Test;

import java.util.*;

public class D4_层序遍历 {


    @Test
    public void dgTest() {
        TreeNode root = initTreeNode();
        System.out.println("层序遍历 从上到下，从右到左");
        ArrayList arrayList = new ArrayList();
        arrayList.add(root);
        dgRightToLeft(arrayList);
        System.out.println();
        System.out.println("层序遍历 从上到下，从左到右");
    }


    /**
     * 递归实现 层序遍历（上到下，从从右到左）：
     * 队列的解决方案，将每一行的数据放到队列中，依次打印出来
     */
    public  void dgRightToLeft(ArrayList<TreeNode> list) {
        if (list.isEmpty()){
            return;
        }
        ArrayList<TreeNode> newList = new ArrayList<>();
        for(TreeNode node:list){
            System.out.println(node.val);

            if (node.right != null){
                newList.add(node.right);
            }
            if (node.left != null){
                newList.add(node.left);
            }

        }
        dgRightToLeft(newList);
    }


    @Test
    public void test() {
        TreeNode root = initTreeNode();
        System.out.println("层序遍历 从上到下，从右到左");
        rightToLeft(root);
        System.out.println();
        System.out.println("层序遍历 从上到下，从左到右");
        leftToRigit(root);
    }



    /**
     * 层序遍历（上到下，从从右到左）：
     * 队列的解决方案，将每一行的数据放到队列中，依次打印出来
     */
    public static void rightToLeft(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            //表示每行有多少个
            int hangSize = queue.size();
            //遍历每行的数据
            while (hangSize > 0) {
                //从队列中取出，打印根节点
                TreeNode node  = queue.remove();
                System.out.println(node.val);
                hangSize--;

                if (node.right != null) {
                    queue.add(node.right);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
            }
        }
    }


    /**
     * 层序遍历（上到下，从从左到右）：
     * 队列的解决方案，将每一行的数据放到队列中，依次打印出来
     */
    public void leftToRigit(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int i = queue.size();
            while (i > 0) {
                TreeNode node = queue.remove();
                System.out.println(node.val);
                i--;
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
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
