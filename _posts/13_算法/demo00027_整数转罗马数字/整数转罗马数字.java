package com.hlj.arith.demo00027_整数转罗马数字;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 整数转罗马数字
 * @date 2020/2/25  12:27.
 * @Description
 */
/**
作者：HealerJean
题目：整数转罗马数字
解题思路： 找规律
复杂度：
 时间复杂度：O(1)，虽然看起来是两层循环，但是外层循环的次数是有限制的，因为数字最大才3999，内层循环的此时其实也是有限次的
 空间复杂度：O(1)，这里使用了两个辅助数字，空间都为 1313，还有常数个变量

*/
public class 整数转罗马数字 {

    @Test
    public void test(){
        System.out.println(intToRoman(11));
    }


    public String intToRoman(int num) {
        // 把阿拉伯数字与罗马数字可能出现的所有情况和对应关系，放在两个数组中，按照阿拉伯数字的大小降序排列，因为我们要尽可能的做减法
        int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romans = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int length = nums.length;
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        while (index < length) {
            // 这里是等号，这样就保证了那种特殊的数字
            while (num >= nums[index]) {
                // 注意：这里是等于号，表示尽量使用大的"面值"
                stringBuilder.append(romans[index]);
                num = num -  nums[index];
            }
            index++;
        }
        return stringBuilder.toString();
    }

}
