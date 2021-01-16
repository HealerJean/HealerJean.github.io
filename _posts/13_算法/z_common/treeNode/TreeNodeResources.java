package com.hlj.arith.z_common.treeNode;

import com.hlj.arith.demo00107_将有序数组转换为二叉搜索树.将有序数组转换为二叉搜索树;
import org.junit.Test;

import java.util.*;

/**
 * @author HealerJean
 * @ClassName TreeNodeResources
 * @date 2020/5/9  10:26.
 * @Description
 */
public class TreeNodeResources {




    @Test
    public void test(){

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
