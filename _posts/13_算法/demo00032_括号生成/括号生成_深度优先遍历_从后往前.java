package com.hlj.arith.demo00032_括号生成;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
public class 括号生成_深度优先遍历_从后往前 {

    @Test
    public void test(){
        System.out.println(generateParenthesis(2));
    }

    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        //一对括号也没有，返回空集合
        if (n == 0) {
            return res;
        }


       // 执行深度优先遍历，搜索可能的结果
        dfs("", n, n, res);
        return res;
    }

    /**
     * @param curStr 当前递归得到的结果
     * @param left   左括号还有几个可以使用
     * @param right  右括号还有几个可以使用
     * @param res    结果集
     */
    private void dfs(String curStr, int left, int right, List<String> res) {
        // 在递归终止的时候，,当左右括号全部用完，直接把它添加到结果集即可，
        if (left == 0 && right == 0) {
            res.add(curStr);
            return;
        }

        // 剪枝（如图，左括号可以使用的个数严格大于右括号可以使用的个数，才剪枝，注意这个细节）
        if (left > right) {
            return;
        }

        //如果左节点或者右节点大于 0 ，则添加即可
        if (left > 0) {
            dfs(curStr + "(", left - 1, right, res);
        }
        if (right > 0) {
            dfs(curStr + ")", left, right - 1, res);
        }
    }



}


