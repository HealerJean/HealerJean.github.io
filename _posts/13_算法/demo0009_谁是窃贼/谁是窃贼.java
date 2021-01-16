package com.hlj.arith.demo0009_谁是窃贼;


import org.junit.Test;

/**
作者：HealerJean
题目：谁是窃贼
    警察局抓住了A、B、C、D四名盗窃嫌疑犯，其中只有一人是小偷。在审问时，
    A说：“我不是小偷”；
    B说：“C是小偷”；
    C说：“小偷肯定是D”；
    D说：“C在冤枉好人”。
    现在已经知道这四人中有三人说的是真话，一人说的是假话。请问到底谁是小偷
解题思路：
    设4个变量a,b,c,d，为0时表示不是小偷，为1时表示是小偷
 */
public class 谁是窃贼 {

    @Test
    public void start() {
        int A, B, C, D;
        for (A = 0; A <= 1; A++) {
            for (B = 0; B <= 1; B++) {
                for (C = 0; C <= 1; C++) {
                    for (D = 0; D <= 1; D++) {
                        //有一个人是小偷
                        if (A + B + C + D == 1) {
                            int sum = 0;
                            //A不是小偷
                            if (A == 0) {
                                sum = sum + 1;
                            }
                            //C是小偷
                            if (C == 1) {
                                sum = sum + 1;
                            }
                            //D是小偷
                            if (D == 1) {
                                sum = sum + 1;
                            }
                            //D不是小偷
                            if (D == 0) {
                                sum = sum + 1;
                            }
                            //三人说真话
                            if (sum == 3) {
                                if (A == 0) {
                                    System.out.println("A不是小偷\n");
                                } else {
                                    System.out.println("A是小偷\n");
                                }
                                if (B == 0) {
                                    System.out.println("B不是小偷\n");
                                } else {
                                    System.out.println("B是小偷\n");
                                }
                                if (C == 0) {
                                    System.out.println("C不是小偷\n");
                                } else {
                                    System.out.println("C是小偷\n");
                                }
                                if (D == 0) {
                                    System.out.println("D不是小偷\n");
                                } else {
                                    System.out.println("D是小偷\n");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
