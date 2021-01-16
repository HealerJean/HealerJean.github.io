package com.hlj.arith.demo00099_模式匹配;

import org.junit.Test;

/**
作者：HealerJean
题目：
 你有两个字符串，即pattern和value。 pattern字符串由字母"a"和"b"组成，用于描述字符串中的模式。例如，字符串"catcatgocatgo"匹配模式"aabab"（其中"cat"是"a"，"go"是"b"），该字符串也匹配像"a"、"ab"和"b"这样的模式。但需注意"a"和"b"不能同时表示相同的字符串。编写一个方法判断value字符串是否匹配pattern字符串。
     提示： 1、0 <= len(pattern) <= 1000
            2、0 <= len(value) <= 1000
            3、你可以假设pattern只包含字母"a"和"b"，value仅包含小写字母。
     示例 1：
         输入： pattern = "abba", value = "dogcatcatdog"
         输出： true
     示例 2：
         输入： pattern = "abba", value = "dogcatcatfish"
         输出： false
     示例 3：
         输入： pattern = "aaaa", value = "dogcatcatdog"
         输出： false
     示例 4：
         输入： pattern = "abba", value = "dogdogdogdog"
         输出： true
         解释： "a"="dogdog",b=""，反之也符合规则

解题思路：
*/
public class 模式匹配 {

    @Test
    public void test(){
        System.out.println(patternMatching("abba", "dogcatcatdog"));
    }

    public boolean patternMatching(String pattern, String value) {
        int a_count = 0, b_count = 0;
        // 获取 pattern 中  a 和 b 的个数
        for (char ch : pattern.toCharArray()) {
            if (ch == 'a') {
                a_count++;
            } else {
                b_count++;
            }
        }

        // 首先保证 pattern 中 a 出现的次数不少于 b 出现的次数。如果不满足，我们就将 a 和 b 互相替换；
        if (a_count < b_count) {
            //交换a和b的值
            int temp = a_count;
            a_count = b_count;
            b_count = temp;

            // pattern 交换a和b的值
            char[] array = pattern.toCharArray();
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i] == 'a' ? 'b' : 'a';
            }
            pattern = new String(array);
        }

        //如果value 的长度为0  ，因为 a 个数 不小于 b的个数，
        // 所以 当b的个数为 0 的时候。a可以为空串，也能匹配，反而如果b的个数也大于 0 ，a"和"b"不能同时表示相同的字符串，则肯定不会匹配
        if (value.length() == 0) {
            return b_count == 0;
        }
        // 到了这里 说明 value 有值，pattern 长度为0 则肯定返回false
        if (pattern.length() == 0) {
            return false;
        }

        //能走到下面 说明肯定存在字符串a，字符串b不一定
        // 因为a可以是空串，所以a的长度从0开始
        // a的长度  *  a的个数 必然小于待匹配的字符串 ，
        for (int a_len = 0; a_count * a_len <= value.length(); a_len++) {
            // 待匹配的字符串减去a的总长度 为 b的总长度
            int b_total_len = value.length() - a_count * a_len;

            //只有可能有解才进入
            // b_count == 0 && b_total_len == 0 ：只有字符串a的情况
            // b_count != 0 && rest % b_count == 0 有a有b的情况，并且剩余可以整除b的个数
            if ((b_count == 0 && b_total_len == 0) || (b_count != 0 && b_total_len % b_count == 0)) {
                int b_len = (b_count == 0 ? 0 : b_total_len / b_count);
                //a 和 b字符串
                String a_str = "", b_str = "";
                //是否匹配
                boolean flag = true;
                //索引
                int index = 0;
                for (char ch : pattern.toCharArray()) {
                    if (ch == 'a') {
                        String str = value.substring(index, index + a_len);
                        //首次进入，str赋值给 a_str
                        if (a_str.length() == 0) {
                            a_str = str;
                        } else if (!a_str.equals(str)) {
                            // 跳出当前for循环，继续寻找下一个a的长度
                            flag = false;
                            break;
                        }
                        index += a_len;
                        //非a即 b
                    } else {
                        String str = value.substring(index, index + b_len);
                        //首次进入，str赋值给 b_str
                        if (b_str.length() == 0) {
                            b_str = str;
                         // 第二次进入 ，如果不相等的haul，就结束吧
                        } else if (!b_str.equals(str)) {
                            flag = false;
                            break;
                        }
                        index += b_len;
                    }
                }

                //如果flag成立，并且保证题目说的 a和b不能是相同的字符串
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }


}
