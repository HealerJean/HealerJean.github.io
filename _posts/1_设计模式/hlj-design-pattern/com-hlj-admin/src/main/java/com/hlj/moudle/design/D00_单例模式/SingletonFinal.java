package com.hlj.moudle.design.D00_单例模式;

/**
 * @author HealerJean
 * @date 2020/12/3  10:29.
 * @description
 */

public class SingletonFinal {

    private static SingletonFinal instance = null;

    private SingletonFinal() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SingletonFinal();
        }
    }

    public static SingletonFinal getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }
}
