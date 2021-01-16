package com.hlj.arith.demo00109_跳水板;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
作者：HealerJean
题目：
 你正在使用一堆木板建造跳水板。有两种类型的木板，其中长度较短的木板长度为shorter，长度较长的木板长度为longer。你必须正好使用k块木板。编写一个方法，生成跳水板所有可能的长度。
 返回的长度需要从小到大排列。
     提示：
         0 < shorter <= longer
         0 <= k <= 100000
     示例：
         输入：
             shorter = 1
             longer = 2
             k = 3
         输出： {3,4,5,6}
解题思路：
*/
public class 跳水板 {


    @Test
    public void test(){
        System.out.println(Arrays.toString( divingBoard(1,2,3)));
    }


    /**
     */
    public int[] divingBoard(int shorter, int longer, int k) {
        if (k == 0) {
            return new int[0];
        }
        if (shorter == longer) {
            return new int[]{shorter * k};
        }

        // f(i) =  shorter * (k - i) +  longer * i;  // 短木板 + 长木板
        // => 我们知道函数 f(i) 不会有相同的取值。而 i 的取值是 0 <= i <= k，因此 f(i) 必有 k + 1 个不同的取值。因此我们定义一个长度为 k + 1 的数组，把其中的每个位置分别设置为 shorter * (k - i) + longer * i 即可。
        int[] nums = new int[k + 1];
        //  题目要求，返回的长度需要从小到大排列。所以先用短木板，再用长木板，所以
        // 当i=0时,全使用短木板     shorter * (k - i) =>  shorter * k
        // 当i=k时,全使用长木板      longer *   i     =>  longer * k
        for (int i = 0; i <= k; i++) {
            nums[i] = shorter * (k - i) + longer * i;

        }
        return nums;
    }


}
