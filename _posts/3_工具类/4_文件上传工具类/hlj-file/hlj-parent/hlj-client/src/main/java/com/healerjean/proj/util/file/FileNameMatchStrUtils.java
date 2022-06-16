package com.healerjean.proj.util.file;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangyujin
 * @date 2022/4/7  18:45.
 * @description
 */
@Slf4j
public class FileNameMatchStrUtils {


    private static final Set<String> existList = new HashSet<>();

    @Test
    public void test() throws Exception {

        List<String> jsfList = Lists.newArrayList(".gitignore");
        List<String> list = new ArrayList<>(jsfList);

        File file = new File("/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io");
        File[] files = file.listFiles();
        for (File f : files) {
            // log.info("-------name{}", f.getName());

            cycleFileContent(f, list);
        }
        log.info("=======================");

        Thread.sleep(5000L);
    }


    public void cycleFileContent(File file, List<String> list) throws Exception {
        if (file.isFile()){
            for (String content : list) {
                if (file.getName().equals(content)) {
                    file.delete();
                }
            }
            return;
        }

        File[] files = file.listFiles();
        if (files == null){
            return;
        }

        for (File f : files) {
            cycleFileContent(f, list);
        }
    }



}
