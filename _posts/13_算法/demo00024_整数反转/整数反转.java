package com.hlj.arith.demo00024_整数反转;

import org.junit.Test;

/**
 作者：HealerJean
 题目：整数反转
    注意：假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−2^31,  2^31 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0
 解题思路：一般情况下 除以10 运算即可，但是基于上面的注意，我们一定要考虑整数溢出
    溢出条件有两个，一个是大于整数最大值MAX_VALUE，另一个是小于整数最小值MIN_VALUE，设当前计算结果为 rev，下一位为pop。
        从 rev * 10 + pop > MAX_VALUE这个溢出条件来看
            当出现 `ans > MAX_VALUE / 10 `，则一定溢出   （比如 2147483641）比如：res = 214748365 ->  res * 10 = 2147483650 > 2147483641 溢出
            当出现 `ans == MAX_VALUE / 10` ，但是有余数 且 pop > 7 时，则一定溢出，7是 2^31-1的个位数：比如 res = 214748364  pop > 7  则肯定溢出
        从 `rev * 10 + pop < MIN_VALU`E这个溢出条件来看
            当出现 ans < MIN_VALUE / 10 ，则一定溢出
            当出现 ans == MIN_VALUE / 10 且 pop < -8 时，则一定溢出，8是-2^31的个位数
 */
public class 整数反转 {

    @Test
    public void test(){
        System.out.println("Integer.MIN_VALUE："+Integer.MIN_VALUE);
        System.out.println("Integer.MAX_VALUE："+Integer.MAX_VALUE);
        int a = 2147483647  ;
        System.out.println(a);
        System.out.println(Integer.MAX_VALUE/10 );
        System.out.println(reverse(521));
    }

    public int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            rev = rev * 10 + pop;
            x = x / 10;
        }
        return rev;
    }


}
