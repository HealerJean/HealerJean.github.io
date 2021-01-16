package com.hlj.arith.demo00150_24点游戏;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 你有 4 张写有 1 到 9 数字的牌。你需要判断是否能通过 *，/，+，-，(，) 的运算得到 24。
 示例 1:
     输入: [4, 1, 8, 7]
     输出: True
     解释: (8-4) * (7-1) = 24
 示例 2:
     输入: [1, 2, 1, 2]
     输出: False
解题思路：
*/
public class _24点游戏 {

    @Test
    public void test() {
        int[] nums = {4, 1, 8, 7};
        System.out.println(judgePoint24(nums));
    }

    public boolean judgePoint24(int[] nums) {
        boolean flag = false;
        List<List<Integer>> res = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        //1、获取全排列数据
        dsf(nums, res, new LinkedList<>(), used);


        //2、讲全排列的每组进行判断
        for (List<Integer> list : res){
             flag =  judge(list.get(0),list.get(1),list.get(2),list.get(3) );
            if(flag){
                return true;
            }
        }
        return flag;
    }

    /** 求出全排列数据 */
    public void dsf(int[] nums, List<List<Integer>> res , LinkedList<Integer> linkedList, boolean[] used) {
        if (linkedList.size() == 4) {
            res.add(new ArrayList<>(linkedList));
        }
        for (int i = 0; i < 4; i++) {
            if (used[i]){
                continue;
            }
            used[i] = true;
            linkedList.add(nums[i]);
            dsf(nums, res, linkedList, used);
            linkedList.removeLast();
            used[i] = false;
        }
    }

    // 第二步：由于已经全排列，a、b、c、d 都是等价的，利用四种运算符选出三个数继续
    public boolean judge(double a, double b, double c, double d) {
        return judge(a + b, c, d) ||
                judge(a - b, c, d) ||
                judge(a * b, c, d) ||
                judge(a / b, c, d);
    }

    // 第三步：a 是由两个数组成的，b、c 只表示一个数；a 与 b、c 不等价，b、c 等价
    public boolean judge(double a, double b, double c) {
        // 情况一：a 和 b(c) 组合，a 和 b(c) 不等价， (不等价的有8中组合方式，其中两种是重复的)
        return judge(a + b, c) ||
                judge(a - b, c) ||
                judge(a * b, c) ||
                judge(a / b, c) ||
                // judge(b + a, c) || 其实和上面是重复的
                // judge(b * a, c) ||  其实和上面是重复的
                judge(b - a, c) ||
                judge(b / a, c) ||

                // 情况二：b 和 c 组合 (b和c是等价的)
                judge(a, b + c) ||
                judge(a, b - c) ||
                judge(a, b * c) ||
                judge(a, b / c);
    }

    // 第四步：a 和 b 不等价
    public boolean judge(double a, double b) {
        return Math.abs(a + b - 24) < 0.001 ||
                Math.abs(a - b - 24) < 0.001 ||
                Math.abs(a * b - 24) < 0.001 ||
                Math.abs(a / b - 24) < 0.001 ||

                // Math.abs(b + a - 24) < 0.001 || 其实和上面 Math.abs(a + b - 24) < 0.001 || 是重复的
                // Math.abs(b * a - 24) < 0.001 || 其实和上面  Math.abs(a * b - 24) < 0.001 ||是重复的
                Math.abs(b - a - 24) < 0.001 ||
                Math.abs(b / a - 24) < 0.001;
    }
}
