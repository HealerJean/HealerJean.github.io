package com.hlj.arith.demo00076_从前序与中序遍历序列构造二叉树;

import com.hlj.arith.z_common.treeNode.TreeNodeResources;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
作者：HealerJean
题目：
解题思路：类似于汉诺塔，想象整体

 跟  左   右
 左  跟   右
*/
public class 从前序与中序遍历序列构造二叉树 {


    @Test
    public void test(){
        int[] preorder = {3,9,20,15,7} ;
        int[] inorder = {9,3,15,20,7} ;

        System.out.println(buildTree(preorder, inorder));
    }


    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> hashMap = new HashMap(inorder.length);
        for (int i = 0; i < inorder.length; i++) {
            hashMap.put(inorder[i], i);
        }

        return createTree(preorder, inorder, 0, preorder.length-1, 0, inorder.length-1,  hashMap);
    }

    public TreeNode createTree(int[] preorder, int[] inorder, int p_left_index, int p_right_index, int in_left_index, int in_right_index , Map<Integer, Integer> hashMap) {
        if (p_left_index > p_right_index || in_left_index > in_right_index) {
            return null;
        }

        // 前序遍历中的第一个节点就是根节点
        // 先把中序遍历 中的  根节点找到，然后以这个节点开始创建左右节点
        TreeNode root = new TreeNode(preorder[p_left_index]);

        // 在中序遍历中定位根节点
        // 当然也可以使用map，这样就不用每次都使用for循环读取了
        // int in_root_index = 0 ;
        // for(int i = 0 ; i < inorder.length ; i++){
        //     if (inorder[i] == root.val){
        //         in_root_index = i ;
        //     }
        // }
        int in_root_index = hashMap.get(preorder[p_left_index]);

        // 得到左子树中的节点数目
        int size = in_root_index - in_left_index;

        root.left = createTree(preorder, inorder, p_left_index + 1, p_left_index + size, in_left_index, in_root_index - 1, hashMap);
        root.right = createTree(preorder, inorder, p_left_index + size + 1, p_right_index, in_root_index + 1, in_right_index, hashMap);
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


