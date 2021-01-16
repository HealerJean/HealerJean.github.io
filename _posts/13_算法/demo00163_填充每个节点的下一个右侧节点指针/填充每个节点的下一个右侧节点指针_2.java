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
public class 填充每个节点的下一个右侧节点指针_2 {


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
        if (root == null){
            return root;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Node next = null;
            while (size > 0) {
                size--;
                Node node = queue.remove();
                if (node.right != null){
                    queue.add(node.right);
                }
                if (node.left != null){
                    queue.add(node.left);
                }
                //赋值
                node.next = next;
                next = node;
            }
        }
        return root;
    }


    /**
     * 方法2：官方解答
     * @param root
     * @return
     */
    public Node connect(Node root) {
        if(root == null){
            return root;
        }
        Node curLineNode  = root;
        while (curLineNode != null){
            //构建下一行的链表
            Node nextLineNode = new Node(-1);
            Node nextPre = nextLineNode;

            //然后开始遍历当前层的链表
            while (curLineNode != null){
                //如果当前节点的左子节点不为空，就让pre节点的next指向他，也就是把它串起来
                if (curLineNode.left != null){
                    nextPre.next = curLineNode.left;
                    nextPre = nextPre.next;
                }
                if (curLineNode.right != null){
                    nextPre.next = curLineNode.right;
                    nextPre = nextPre.next;
                }

                //继续访问这一行的下一个节点
                curLineNode = curLineNode.next;
            }

            //把下一层串联成一个链表之后，让他赋值给cur，后续继续循环，直到cur为空为止
            curLineNode = nextLineNode.next;
        }
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
