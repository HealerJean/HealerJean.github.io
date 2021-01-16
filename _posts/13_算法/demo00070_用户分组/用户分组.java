package com.hlj.arith.demo00070_用户分组;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：用户分组
 有 n 位用户参加活动，他们的 ID 从 0 到 n - 1，每位用户都 恰好 属于某一用户组。给你一个长度为 n 的数组 groupSizes，其中包含每位用户所处的用户组的大小，请你返回用户分组情况（存在的用户组以及每个组中用户的 ID）。
     示例 1：
         输入：groupSizes = [3,3,3,3,3,1,3]
         输出：[[5],[0,1,2],[3,4,6]]
         解释：其他可能的解决方案有 [[2,1,6],[5],[0,4,3]] 和 [[5],[0,6,2],[4,3,1]]。
     示例 2：
         输入：groupSizes = [2,1,3,3,3,2]
         输出：[[1],[0,5],[2,3,4]]
解题思路：用一个Map key存储groupSize[i] ，value存储组内的人
*/
public class 用户分组 {

    @Test
    public void test(){
        int[] groupSizes = {2, 2, 1, 1, 1, 1, 1, 1};
        System.out.println(groupThePeople(groupSizes));
    }

    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> list = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int i = 0 ; i < groupSizes.length ; i++){
            if (map.containsKey(groupSizes[i])){
                List<Integer> group = map.get(groupSizes[i]);
                group.add(i);
                if(groupSizes[i]  == group.size()){
                    list.add(group);
                    map.remove(groupSizes[i]);
                }
            }else {
                if (groupSizes[i] == 1 ){
                    list.add(Arrays.asList(i));
                }else {
                    List<Integer> group = new ArrayList<>();
                    group.add(i);
                    map.put(groupSizes[i] , group);
                }
            }
        }
        return list ;
    }

}
