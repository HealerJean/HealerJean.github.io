package com.hlj.arith.demo00163_填充每个节点的下一个右侧节点指针;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
作者：HealerJean
题目：填充每个节点的下一个右侧节点指针
 给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：
 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
 初始状态下，所有 next 指针都被设置为 NULL。
解题思路：
*/
public class 填充每个节点的下一个右侧节点指针_1 {


    @Test
    public void test(){
        System.out.println(connect(initNode()));
    }

    /**
     * 方法1 层序遍历
     * @param root
     * @return
     */
    public Node connect1(Node root) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Node next = null;
            while (size > 0) {
                size--;
                Node node = queue.remove();
                //因为是满二叉树，所以如果有一个为空则退出
                if (node == null){
                    break;
                }
                queue.add(node.right);
                queue.add(node.left);

                //赋值
                node.next = next;
                next = node;
            }
        }
        return root;
    }


    /**
     * 官方解答（有点像翻转二叉树）
     * @param root
     * @return
     */
    public Node connect(Node root) {
        //如果满足以下条件任何一种说明已经在最后一行了（后面两个只要判断一种就可以）
        //root == null 只判断第一个节点
        if (root == null || root.left == null || root.right == null ) {
            return root;
        }

        //连接孩子结点，连接右孩子需要注意，如果root的next为null，则右孩子的next为null，否则为root.next的left
        Node left = root.left;
        Node right = root.right;
        left.next = right;

        //从上到下执行的，所以即使将右节点的next确定
        if (root.next != null) {
            right.next = root.next.left;
        }

        //不会执行到最后一行的
        connect(left);
        connect(right);
        return root;
    }



    public Node initNode(){
        Node node7 = new Node(7, null, null, null);
        Node node6 = new Node(6, null, null, null);
        Node node5 = new Node(5, null, null, null);
        Node node4 = new Node(4, null, null, null);
        Node node3 = new Node(3, node6, node7, null);
        Node node2 = new Node(2, node4, node5, null);
        Node node1 = new Node(1, node2, node3, null);
        return node1 ;
    }

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int val) {
            this.val = val;
        }
        public Node(int val, Node left, Node right, Node next) {
            this.val = val;
             this.left = left;
             this.right = right;
             this.next = next;
        }
    }

}
