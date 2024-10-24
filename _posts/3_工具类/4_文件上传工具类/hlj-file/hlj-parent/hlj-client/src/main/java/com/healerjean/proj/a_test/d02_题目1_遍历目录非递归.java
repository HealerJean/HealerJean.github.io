package com.healerjean.proj.a_test;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/13  下午2:34.
 * 类描述：给定一个根目录，要求遍历其中的文件及子文件夹，返回所有后缀是.txt的文件List
 * * 要求：
 * * 1. 不能使用递归
 */
public class d02_题目1_遍历目录非递归 {

    @Test
    public void test() {
        File root = new File("/Users/healerjean/Desktop/faceFile");
        System.out.println(findTxt(root).toString());
    }

    /**
     * @param root 为一个文件夹根目录
     */
    public List<File> findTxt(File root) {
        List<File> listTxtFiles = new ArrayList<>();
        if (root.exists()) {
            //创建一个目录集合，用于存放跟目录下面的文件夹
            LinkedList<File> directorys = new LinkedList<>();
            //获取根目录下面的子目录文件夹（如果本目录下面包含txt文件，则直接加入listTxtFiles集合中）
            File[] files = root.listFiles();
            for (File file : files) {
                //判断是否为目录，然后
                if (file.isDirectory()) {
                    directorys.add(file);
                } else if (file.getName().endsWith(".txt")) {
                    listTxtFiles.add(new File(file.getAbsolutePath()));
                }
            }

            //创建一个临时目录，用于遍历directorys 集合中一个文件夹，如果是我们需要的txt文件，则直接加入集合中，如果该目录下包含子目录，则继续添加到directorys 集合中
            File temp_file;
            while (!directorys.isEmpty()) {
                //每次遍历directorys 中的第一个目录，遍历之前将它作废
                temp_file = directorys.removeFirst();
                files = temp_file.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        directorys.add(file);
                    } else if (file.getName().endsWith(".txt")) {
                        listTxtFiles.add(new File(file.getAbsolutePath()));
                    }
                }
            }
        }
        return listTxtFiles;
    }

}
