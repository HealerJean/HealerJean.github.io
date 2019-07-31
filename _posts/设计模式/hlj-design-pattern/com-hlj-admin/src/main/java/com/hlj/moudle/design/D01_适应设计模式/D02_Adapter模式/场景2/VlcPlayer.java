package com.hlj.moudle.design.D01_适应设计模式.D02_Adapter模式.场景2;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName VlcPlayer
 * @date 2019/7/31  20:58.
 * @Description
 */
public class VlcPlayer implements AdvancedMediaPlayer{

    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }
}
