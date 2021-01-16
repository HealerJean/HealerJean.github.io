package com.hlj.arith.demo00032_括号生成;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目：括号生成
解题思路：深度优先遍历,从上到下，构建一颗树
 最最需要注意的地方是：剪枝（左括号可以使用的个数严格大于右括号可以使用的个数，才剪枝，注意这个细节）
*/
public class 括号生成_深度优先遍历_从前往后 {

    @Test
    public void test(){
        System.out.println(generateParenthesis(2));
    }

    // 做减法
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        //一对括号也没有，返回空集合
        if (n == 0) {
            return res;
        }

       // 执行深度优先遍历，搜索可能的结果
        dfs("", 0, 0, n, res);
        return res;
    }


    /**
     *
     * @param curStr 当前递归得到的结果
     * @param left 左括号当前使用了几个
     * @param right 右括号当前使用了几个
     * @param n 总共有多少个
     * @param res 结果集
     */
    private void dfs(String curStr, int left, int right,int n, List<String> res) {
        // 构建数完成，直接把它添加到结果集即可，
        if (left == n && right == n) {
            res.add(curStr);
            return;
        }

        // 剪枝（如图，左括号可以使用的个数严格大于右括号可以使用的个数，才剪枝，注意这个细节）
        // if ( (n-left) > (n - right) ) {
        if (right > left) {
            return;
        }

        //如果左节点或者右节点大于 0 ，则添加即可
        if (left < n) {
            dfs(curStr + "(", left + 1, right, n ,res);
        }

        if (right < n) {
            dfs(curStr + ")", left, right + 1, n, res);
        }
    }



}


