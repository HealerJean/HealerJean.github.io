package com.hlj.arith.demo00018_动态规划.Z02_国王挖金矿;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Z02_1_挖金矿_递归
 * @date 2020/1/19  16:04.
 * @Description
 * 当n = 1 ;  w < P[0];        F(n, w) = 0 ；
 * 当n = 1 ;  w >= P[0];       F(n. w) = G[0] ;
 *
 * //通过上面推导出来的 n-1为当前的金矿
 * 当n > 1 ;  w <  P[n - 1];  F(n, w) = F(n - 1, w) ；
 * 当n > 1 ;  w >= P[n - 1];  F(n, w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) ;
 */
public class Z02_1_挖金矿_递归 {

    @Test
    public void diGuimethod() {
        int g[] = new int[]{400, 500, 200, 300, 350};//黄金量
        int p[] = new int[]{5, 5, 3, 4, 3};//人数
        int maxGold = diguiMaxGold(5, 10, g, p);
        System.out.println(maxGold);
    }

    /**
     * 递归算法
     */
    public int diguiMaxGold(int n, int w, int[] g, int[] p) {
        //n 等于 1 的情况
        if (n == 1 && w < p[0]) {
            return 0;
        } else if (n == 1) {
            return g[0];
        }

        //n 大于 1 的情况
        if (w < p[n - 1]) {
            return diguiMaxGold(n - 1, w, g, p);
        }else {
            //挖4座金矿
            int a = diguiMaxGold(n - 1, w, g, p);
            //挖5座金矿
            int b = diguiMaxGold(n - 1, w - p[n - 1], g, p) + g[n-1];
            return Math.max(a, b);
        }


    }


}
