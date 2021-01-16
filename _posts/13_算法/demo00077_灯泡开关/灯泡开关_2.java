package com.hlj.arith.demo00077_灯泡开关;


import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
作者：HealerJean
题目：
 现有一个房间，墙上挂有 n 只已经打开的灯泡和 4 个按钮。在进行了 m 次未知操作后，你需要返回这 n 只灯泡可能有多少种不同的状态。
 假设这 n 只灯泡被编号为 [1, 2, 3 ..., n]，这 4 个按钮的功能如下：

 将所有灯泡的状态反转（即开变为关，关变为开）
 将编号为偶数的灯泡的状态反转
 将编号为奇数的灯泡的状态反转
 将编号为 3k+1 的灯泡的状态反转（k = 0, 1, 2, ...)

 示例 1:
     输入: n = 1, m = 1.
     输出: 2
     说明: 状态为: [开], [关]

 示例 2:
     输入: n = 2, m = 1.
     输出: 3
     说明: 状态为: [开, 关], [关, 开], [关, 关]

 示例 3:
     输入: n = 3, m = 1.
     输出: 4
     说明: 状态为: [关, 开, 关], [开, 关, 开], [关, 关, 关], [关, 开, 开].
解题思路：
 纯找规律
*/
public class 灯泡开关_2 {

    @Test
    public void test(){

        System.out.println(flipLights(1,1));
    }


    public int flipLights(int n, int m) {
        n = Math.min(n, 3);
        int[][] matrix = {
                {1, 1, 1},
                {0, 1, 0},
                {1, 0, 1},
                {1, 0, 0}
        };

        /** 获取 组合集合 */
        List<ArrayList<Integer>> res = new ArrayList<>();
        find(matrix, m, 0, res, new LinkedList<>());
        System.out.println(res);


        /** 获取所有的列表 */
        List<String> collect = getStrings(n, matrix, res);

        System.out.println(collect);
        return collect.size();
    }



    /**
     * 获取不重复的结果
     * @param n 灯泡数量
     * @param matrix
     * @param res
     * @return
     */
    private List<String> getStrings(int n, int[][] matrix, List<ArrayList<Integer>> res) {
        return res.stream().map(item -> {
            // ret 为 最终位置所点击的次数
            int[] value = new int[n];
            for (int i = 0; i < n; i++) {
                int finalI = i;
                item.stream().forEach(j ->  value[finalI] = matrix[j][finalI] + value[finalI]);
            }

            /**  偶数开，奇数关 */
            return Arrays.stream(value).boxed().map(x -> x % 2 == 0 ? "开" : "关")
                    .collect(Collectors.collectingAndThen(Collectors.joining(","), x -> x));
        }).distinct().collect(Collectors.toList());
    }




    /**
     * 获取按m次灯泡的 组合
     * @param matrix
     * @param m 按多少次灯泡
     * @param index
     * @param res
     * @param list
     */
    public void find(int[][] matrix, int m, int index, List<ArrayList<Integer>> res, LinkedList<Integer> list){
        /** 找到5次的 */
        if(list.size() == m){
            res.add(new ArrayList(list));
            return;
        }

        /** 用过就不会回头，但是可以多次使用，所以是index */
        for (int i = index ; i < matrix.length ; i++){
            list.add(i);
            find(matrix,m,i, res,list);
            list.removeLast();
        }
    }



    public int flipLight1(int n, int m) {
        n = Math.min(n, 3);

        //不按开关
        if (m == 0){
            return  1 ;
        }

        //m > 0 的时候
        if (n == 1){
            return  2 ;
        }

        if (n == 2){
            return m == 1 ? 3 : 4 ;
        }

        return m == 1 ? 4 : m==2 ? 7 : 8 ;
    }


}
