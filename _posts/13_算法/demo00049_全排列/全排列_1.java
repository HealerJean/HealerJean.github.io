package com.hlj.arith.demo00049_全排列;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：全排列_1
 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 示例:
     输入: [1,2,3]
     输出:
     [
     [1,2,3],
     [1,3,2],
     [2,1,3],
     [2,3,1],
     [3,1,2],
     [3,2,1]
 解题思路：
*/
public class 全排列_1 {

    @Test
    public void test(){
        int nums[] = {1,2,3,4};
        System.out.println(permute(nums));
    }

    /**
     * 1、使用 集合本身判断是否重复
     */
    public List<List<Integer>> permute2(int[] nums) {
        List<List<Integer>> lists = new ArrayList<>();
        huisu(nums, lists, new LinkedList<>());
        return lists;
    }

    public void  huisu(int[] nums, List<List<Integer>> list, LinkedList<Integer> array){
        if (array.size() == nums.length){
            list.add(new ArrayList<>(array));
            return;
        }
        for (int i = 0 ; i < nums.length; i++){
            //结果集中不能包含重复的
            if (array.contains(nums[i])){
                continue;
            }
            array.add(nums[i]);
            huisu(nums, list, array);
            //删除后一个节点，向上回溯
            array.removeLast();
        }
    }


    /**
     * 2、开启一个新的数组判断是否重复
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> lists = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        huisu(nums,used, lists, new LinkedList<>());
        return lists;
    }

    public void  huisu(int[] nums,boolean[] used, List<List<Integer>> list, LinkedList<Integer> array){
        if (array.size() == nums.length){
            list.add(new ArrayList<>(array));
            return;
        }
        for (int i = 0 ; i < nums.length; i++){
            //结果集中不能包含吃不惯分页
            // if (array.contains(nums[i])){
            //     continue;
            // }
            if (used[i] == true){
                continue;
            }
            array.add(nums[i]);
            used[i] = true ;
            huisu(nums,used, list, array);
            //删除后一个节点，向上回溯
            used[i] = false ;
            array.removeLast();
        }
    }




}
