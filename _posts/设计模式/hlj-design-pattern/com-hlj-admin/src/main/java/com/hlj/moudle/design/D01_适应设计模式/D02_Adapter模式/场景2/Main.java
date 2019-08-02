package com.hlj.moudle.design.D01_适应设计模式.D02_Adapter模式.场景2;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName Main
 * @date 2019/7/31  20:59.
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "beyond the horizon.mp3");
        audioPlayer.play("mp4", "alone.mp4");
        audioPlayer.play("vlc", "far far away.vlc");
        audioPlayer.play("avi", "mind me.avi");
    }
}
