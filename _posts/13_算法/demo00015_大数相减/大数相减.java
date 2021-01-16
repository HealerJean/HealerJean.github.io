package com.hlj.arith.demo00015_大数相减;


import org.junit.Test;

import java.util.Arrays;
import java.util.Scanner;


/**
作者：HealerJean
题目：有 N 行测试数据，每一行有两个代表整数的字符串 a 和 b，长度超过百位。规定 a>=b，a, b > 0。 测试结果可以用 linux 小工具 bc进行测试是否正确。
     输出：返回表示结果整数的字符串。
     输入样例：1231231237812739878951331231231237812739878951331231231237812739878951331231231237812739878951331231231237812739878951331231231237812739870-895133123123123781273987895133123123123781273987895133123123123781273987895131231231237812739878951331231231237812739878951331231231237812739878951331230000000000000000000000001-331231231237812739878951331231231
     输出样例：12312312378127398789513312312312378127398789513312312312378126503656390189188531104139503656390189188531104139503656390189188531104139503571231231237812739878951331231231237812739878951331231231237812739878620099998762187260121048668768770
解题思路：

 */
public class 大数相减 {


    public static void main(String args[]) {

        Scanner scan = new Scanner(System.in);
        String line;
        while (scan.hasNextLine()) {
            line = scan.nextLine().trim();
            String[] lineArray = line.split("-");

            Integer a[] = Arrays.stream(lineArray[0].split("")).map(Integer::valueOf).toArray(Integer[]::new);
            Integer b[] = Arrays.stream(lineArray[1].split("")).map(Integer::valueOf).toArray(Integer[]::new);
            Integer f[] = new Integer[a.length];
            int n = 0;
            int aIndex = a.length - 1;
            int bIndex = b.length - 1;

            while (bIndex > -1) {
                //如果a当前索引位 大于等于 b，则直接相减 否则就要借位了哦
                if (a[aIndex] >= b[bIndex]) {
                    f[n] = (a[aIndex] - b[bIndex]);
                } else {
                    //因为肯定是要借位的，所以在a索引位直接借位 +10  在减去b索引位为，然后，开始借位重新计算a数组的值
                    f[n] = a[aIndex] + 10 - b[bIndex];
                    jiewei(a, aIndex);
                }
                aIndex--;
                bIndex--;
                n++;
            }

            //a肯定会有剩余的，所以需要讲a也放到最终的数组中去
            while (aIndex > -1){
                f[n] = a[aIndex];
                aIndex--;
                n++;
            }

            //开始倒叙输出，因为有null的值，需要清理
            StringBuilder sb = new StringBuilder();
            int s = a.length - 1;
            for (; s > -1; s--) {
                if (f[s] != null) {
                    sb.append(f[s]);
                }
            }
            System.out.println(sb);
        }
    }



    /**
     * 借位 比如输入 [1000] i =3  => 0990
     */
    public static void jiewei(Integer fox[], int index) {
        if (fox[index - 1] > 0) {
            fox[index - 1] = fox[index - 1] - 1;
        } else {
            fox[index - 1] = 9;
            jiewei(fox, index - 1);
        }
    }

}
