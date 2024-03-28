package com.healerjean.proj.a_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangyujin
 * @date 2023/6/13  16:25.
 */
@Slf4j
public class D05_FileRename {


    /**
     * 家庭 (秒转时间)
     */
    @Test
    public void testHome(){
        File file = new File("/Users/healerjean/Desktop/HealerJean/HHome/家庭");
        testHomeCycleFileRename(file);
    }

    public void testHomeCycleFileRename(File file) {
        File[] files = file.listFiles();
        for (File sonFile : files) {
            if (sonFile.isDirectory()) {
                testHomeCycleFileRename(sonFile);
            } else {
                if (sonFile.getName().endsWith(".jpeg")){
                    sonFile.delete();
                }
                if (sonFile.getName().endsWith(".mp4")){
                    String name = sonFile.getName().substring(0, sonFile.getName().indexOf("."));
                    String[] split = name.split("_");
                    Long i = Long.parseLong(split[1]);
                    String prefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(i * 1000L));
                    String newName = prefix +"_" +split[0] + ".mp4";
                    System.out.println("原名称:" + name +",新名称:"+  newName);
                    sonFile.renameTo(new File("/Users/healerjean/Desktop/HealerJean/HHome/家庭-修正/" +newName ));
                }
                // log.info(sonFile.getAbsolutePath());
            }
        }
        // log.info(file.getAbsolutePath());
    }





    /**
     * rename VID20230726184343.mp4 ——> VID_20230726_184343.mp4
     */
    @Test
    public void testRename1(){
        File file = new File("/Volumes/H_A/vedio/0_个人/1_妈妈（截止20240128）/视频");
        testRename1CycleFileRename2(file);
    }
    public void testRename1CycleFileRename2(File file) {
        File[] files = file.listFiles();
        for (File sonFile : files) {
            if (sonFile.isDirectory()) {
                testHomeCycleFileRename(sonFile);
            } else {
                if (sonFile.getName().startsWith("VID")){
                    String start = sonFile.getName().substring(0, 11);
                    String end = sonFile.getName().substring(11);
                    String newName = (start + "_" + end).replace("VID", "VID_");
                    System.out.println(newName);
                    sonFile.renameTo(new File("/Volumes/H_A/vedio/0_个人/1_妈妈（截止20240128）/视频/" +newName ));
                }
            }
        }
    }


    /**
     * rename wx_camera_1678420143477.mp4 ——> WX_20230726_184343.mp4
     */
    @Test
    public void testRename2(){
        String pathMm = "/Volumes/H_A/vedio/0_个人/1_妈妈（截止20240128）/视频";
        String pathLp = "/Volumes/H_A/vedio/0_个人/2_老婆（截止20240128）/视频";
        File file = new File(pathLp);
        testRename2CycleFileRename2(file, pathLp);
    }
    public void testRename2CycleFileRename2(File file, String path) {
        File[] files = file.listFiles();
        for (File sonFile : files) {
            if (sonFile.isDirectory()) {
                testRename2CycleFileRename2(sonFile, path);
            } else {
                String name = sonFile.getName();
                if (name.contains("(")){
                    sonFile.delete();
                }else {
                    if (name.startsWith("wx_camera")){
                        String[] split = sonFile.getName().split("_");
                        String prefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(Long.parseLong(split[2].replace(".mp4",""))));
                        String newName = prefix + ".mp4";
                        System.out.println(newName);
                        sonFile.renameTo(new File(path +"/" +newName ));
                    }
                }
            }
        }
    }


    /**
     * rename
     */
    @Test
    public void testRename(){
        String pathMm = "/Volumes/H_A/vedio/0_个人/1_妈妈（截止20240128）/视频";
        String pathLp = "/Volumes/H_A/vedio/0_个人/2_老婆（截止20240128）/视频";
        String tempPath = "/Volumes/H_A/vedio/0_个人/2_老婆（截止20240128）";

        File file = new File(tempPath);
        doRestRename(file, tempPath);
    }

    public void doRestRename(File file, String path) {
        File[] files = file.listFiles();
        for (File sonFile : files) {
            String name = sonFile.getName();
            if (name.startsWith("视频")) {
                String newName = name.replace("视频", "");
                System.out.println(newName);
                // sonFile.renameTo(new File(path + "/" + newName));
            }
        }
    }

}
