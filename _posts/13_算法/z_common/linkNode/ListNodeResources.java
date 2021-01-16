package com.hlj.arith.z_common.linkNode;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName ListNodeResources
 * @date 2020/5/9  12:22.
 * @Description
 */
public class ListNodeResources {

    @Test
    public void test(){
        String str = listNodeStr(listNode(),"");
        System.out.println(str);
    }

    public ListNode listNode(){
        ListNode listNode_5 = new ListNode(5, null);
        ListNode listNode_4 = new ListNode(4, listNode_5);
        ListNode listNode_3 = new ListNode(2, listNode_4);
        ListNode listNode_2 = new ListNode(1, listNode_3);
        ListNode listNode_1 = new ListNode(0, listNode_2);
        return listNode_1;
    }

    public String listNodeStr(ListNode listNode, String str){
        if (listNode == null){
            return str.substring(0, str.lastIndexOf(","));
        }
        str = str + listNode.val + "," ;
        return listNodeStr(listNode.next, str);
    }

    class ListNode{
        int val;
        ListNode next ;
        public ListNode(int val) {
            this.val = val;
        }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
