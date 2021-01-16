package com.hlj.arith.demo00091_等式方程的可满足性;

import org.junit.Test;

/**
作者：HealerJean
题目：等式方程的可满足性
 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，并采用两种不同的形式之一："a==b" 或 "a!=b"。在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。 
     示例 1：
         输入：["a==b","b!=a"]
         输出：false
         解释：如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。
     示例 2：
         输入：["b==a","a==b"]
         输出：true
         解释：我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。
     示例 3：
         输入：["a==b","b==c","a==c"]
         输出：true
     示例 4：
         输入：["a==b","b!=c","c==a"]
         输出：false
     示例 5：
         输入：["c==c","b==d","x!=z"]
         输出：true

解题思路：
*/
public class 等式方程的可满足性 {

    @Test
    public void test(){
        String[] equations = {"a==b","a==a"} ;
        System.out.println(equationsPossible(equations));
    }

    public boolean equationsPossible(String[] equations) {
        // 初始化一开始所有变量的父节点都是它们自身
        //后续的过程中，在find查找过程中，我们沿着当前变量的父节点一路向上查找，直到找到根节点（并且将结果赋值）
        int[] parent = new int[26];
        for (int i = 0; i < 26; i++) {
            parent[i] = i;
        }

        //26个英文字母各代表 1,2,3,4,5…… 26
        for (String str : equations) {
            if (str.charAt(1) == '=') {
                int index1 = str.charAt(0) - 'a';
                int index2 = str.charAt(3) - 'a';
                parent[find(parent, index1)] = find(parent, index2);
            }
        }


        for (String str : equations) {
            if (str.charAt(1) == '!') {
                int index1 = str.charAt(0) - 'a';
                int index2 = str.charAt(3) - 'a';
                // 一直走到跟节点，如果是相等的话，则表示不等式不成立
                if (find(parent, index1) == find(parent, index2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 一直走到最后的跟节点、
     */
    public int find(int[] parent, int index) {
        while (parent[index] != index) {
            parent[index] = parent[parent[index]];
            index = parent[index];
        }
        return index;
    }


}
