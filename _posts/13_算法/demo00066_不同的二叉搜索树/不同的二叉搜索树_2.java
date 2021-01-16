package com.hlj.arith.demo00066_不同的二叉搜索树;


import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个整数 n，生成所有由 1 ... n 为节点所组成的 二叉搜索树 。
 示例：

     输入：3
     输出：
     [
       [1,null,3,2],
       [3,2,null,1],
       [3,1,null,null,2],
       [2,1,3],
       [1,null,2,null,3]
     ]
     解释：
     以上的输出对应以下 5 种不同结构的二叉搜索树：

     1         3     3      2      1
     \       /     /      / \      \
     3     2     1      1   3      2
     /     /       \                 \
     2     1         2                 3
解题思路：
*/
public class 不同的二叉搜索树_2 {

    @Test
    public void test(){
        System.out.println(generateTrees(3));
    }


    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        return generateTrees(1, n);
    }


    /**
     * 获取 从 start 到 end 所有二叉树的集合
     */
    public List<TreeNode> generateTrees(int start, int end) {
        List<TreeNode> list = new ArrayList<>();
        // 当前二叉搜索树为空，（因为我们要打印 null，所以需要 list.add(null) ）返回空节点即可。
        if (start > end) {
            list.add(null);
            return list;
        }

        // 遍历所有的节点
        for (int i = start; i <= end; i++) {
            // 获得 以 i 为跟节点 所有的左子树集合
            List<TreeNode> leftList = generateTrees(start, i - 1);

            // 获得 以 i 为跟节点 所有的右子树集合
            List<TreeNode> rightList = generateTrees(i + 1, end);


            // 从左子树集合中选出一棵左子树，从右子树集合中选出一棵右子树，拼接到根节点上
            for (TreeNode left : leftList) {
                for (TreeNode right : rightList) {
                    TreeNode node = new TreeNode(i);
                    node.left = left;
                    node.right = right;
                    list.add(node);
                }
            }
        }
        return list;
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
