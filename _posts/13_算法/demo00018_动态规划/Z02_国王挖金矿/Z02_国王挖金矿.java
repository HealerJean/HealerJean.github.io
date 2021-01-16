package com.hlj.arith.demo00018_动态规划.Z02_国王挖金矿;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Z02_国王挖金矿
 * @date 2020/4/28  18:08.
 * @Description
 */
public class Z02_国王挖金矿 {


    @Test
    public void test(){
        //黄金量
        int g[] = new int[]{100, 500, 200, 300, 350};
        //人数
        int p[] = new int[]{5, 5, 3, 4, 3};


        int res = diguiMaxGold( 5, 10, g,p );
        System.out.println(res);
    }

    /**
     *
     * @param g
     * @param p
     * @param i 金矿
     * @param j
     * @return
     */
    public int diguiMaxGold( int i, int j,int[] g,int[] p ) {

        if (i == 1 && j < p[i-1]){
            return 0 ;
        }else if (i == 1){
            return  g[i-1];
        }

        if (j < p[i-1]){
           return  diguiMaxGold(i-1, j,g, p) ;
        }else {
            int a =  diguiMaxGold(i-1, j , g, p) ;
            int b =  diguiMaxGold(i-1, j - p[i-1], g, p)+ g[i-1] ;
            return Math.max(a, b);
        }
    }

}
