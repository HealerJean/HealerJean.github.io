package com.hlj.arith.demo00079_和可被K整除的连续子数组;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
作者：HealerJean
题目：
 给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。
 示例：
     输入：A = [4,5,0,-2,-3,1], K = 5
     输出：7
     解释：
     有 7 个子数组满足其元素之和可被 K = 5 整除：
     [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
解题思路：
 如果sum1%k=n，sum2%k=n，那么(sum2-sum1)%k=0
*/
public class 和可被K整除的连续子数组 {

    @Test
    public void test(){
        int[] A = {4,5,0,-2,-3,1} ;
        int K = 5 ;

        System.out.println(-6%5);  //= -1
        System.out.println(subarraysDivByK(A, K));
    }

    public int subarraysDivByK(int[] A, int K) {
        Map<Integer,Integer> map = new HashMap();
        // 余数为0，表示当前到达的位置本身就能整除。（相当于有几个 0， 集合就多几个。加入有n个0，那么最终的结果是 n + n(n-1)/2 = (n+1)n/2 ，所以我们提前放入一个
        map.put(0,1);
        int sum = 0;
        for(int num : A){
            sum += num;
            //当被除数为负数时取模结果为负数，需要纠正
            // 这个地方非常巧妙， 比如如果是 -1%5  余数为-1 。 如果是-4%5 余数为 -4 。但是这里都是负数，不好参与
            // （正数 + k）% k  不会影响值
            // （负数 + k）$ k  会是一个正数(找到最接近0的正数)。
            //  [-1,2,9] 2 => 如果是 正常的sum%k的情况，那么本来2就是一个结果集，但是缺由于余数为-1会有问题
            int mod = (sum % K + K) % K;
            map.put(mod, map.getOrDefault(mod,0) + 1);
        }
        int count = 0;
        for(int num : map.keySet()){
            // 排列组合，从同余里取2个数，除去有序情况
            count += map.get(num) * (map.get(num)-1) / 2;
        }
        return count;
    }

}
