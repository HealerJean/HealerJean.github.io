package com.hlj.moudle.design.D01_适应设计模式.D02_Adapter模式.场景2;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName Mp4Player
 * @date 2019/7/31  20:59.
 * @Description
 */
public class Mp4Player implements AdvancedMediaPlayer{

    @Override
    public void playVlc(String fileName) {
        //什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: "+ fileName);
    }
}
