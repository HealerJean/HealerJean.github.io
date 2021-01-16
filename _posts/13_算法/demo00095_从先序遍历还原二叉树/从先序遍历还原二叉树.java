package com.hlj.arith.demo00095_从先序遍历还原二叉树;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 我们从二叉树的根节点 root 开始进行深度优先搜索。
 在遍历中的每个节点处，我们输出 D 条短划线（其中 D 是该节点的深度），然后输出该节点的值。（如果节点的深度为 D，则其直接子节点的深度为 D + 1。根节点的深度为 0）。
 如果节点只有一个子节点，那么保证该子节点为左子节点。
 给出遍历输出 S，还原树并返回其根节点 root。
     示例 1：
         输入："1-2--3--4-5--6--7"
         输出：[1,2,5,3,4,6,7]
     示例 2：
         输入："1-2--3---4-5--6---7"
         输出：[1,2,5,3,null,6,null,4,null,7]
     示例 3：
    `     输入："1-401--349---90--88"
         输出：[1,401,null,349,88,90]
解题思路：
*/
public class 从先序遍历还原二叉树 {

    @Test
    public void test(){
        String s = "1-2--3--4-5--6--7";
        TreeNode treeNode = recoverFromPreorder(s);
    }


    public TreeNode recoverFromPreorder(String str) {

        // 存储必要的节点。
        Stack<TreeNode> stack = new Stack<>();
        // 存储字符串中的位置
        int i = 0;
        while (i < str.length()) {

            // 1、获取当前层数level
            int level = 0;
            while (str.charAt(i) == '-') {
                level++;
                i++;
            }

            // 2、获取节点值
            StringBuilder numsStr = new StringBuilder();
            while (i < str.length() &&  Character.isDigit(str.charAt(i))) {
                numsStr.append(str.charAt(i));
                i++;
            }
            int val = Integer.valueOf(numsStr.toString());


            // 构造当前节点
            TreeNode node = new TreeNode(val);

            //如果当前节点的深度==当前路径长度 => 前一个节点是当前节点的父节点）
            if (level == stack.size()) {
                //第一个跟节点到这里的时候，栈里面还没有数据，所以这里面需要判断
                if (!stack.isEmpty()) {
                    //前一个节点的左子节点为当前节点
                    stack.peek() .left = node;
                }

             //能够到这个false 栈中的数据肯定比leve大
            } else {
                //前一个节点不是当前节点的父节点 ,则一直出栈，直到找到父节点
                while (level != stack.size()) {
                    //通过queue弹出其他子节点，找到当前节点的父节点
                    stack.pop();
                }

                // 找到父节点，因为此时左子节点已确定，所以赋值给右子节点
                stack.peek().right = node;
            }

            // 放入栈中
            stack.push(node);
        }



        // 全部弹出，只剩根节点
        while (stack.size() > 1) {
            stack.pop();
        }
        return stack.peek();
    }






    public TreeNode initTreeNode() {
        TreeNode treeNode1 = new TreeNode(3, null, null);
        TreeNode treeNode2 = new TreeNode(6, null, null);
        TreeNode treeNode3 = new TreeNode(4, treeNode1, treeNode2);
        TreeNode treeNode4 = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(5, treeNode3, treeNode4);
        return root;
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
