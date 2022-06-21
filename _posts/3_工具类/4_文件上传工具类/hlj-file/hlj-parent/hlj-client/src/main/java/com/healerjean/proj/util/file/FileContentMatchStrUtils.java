package com.healerjean.proj.util.file;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
public class FileContentMatchStrUtils {


    private static final Set<String> existList = new HashSet<>();

    @Test
    public void test() throws Exception {

        // List<String> jsfList = readList(new File("/Users/healerjean/Desktop/jsf.txt"));
        // List<String> mqList = readList(new File("/Users/healerjean/Desktop/mq.txt"));
        List<String> list = Lists.newArrayList("QLExpress");

        // List<String> list = new ArrayList<>();
        // list.addAll(jsfList);
        // list.addAll(mqList);

        File originFile = new File("/Users/healerjean/Desktop/HealerJean/A_Company/MOKA/code");
        for (File file : originFile.listFiles()) {
                log.info("SSSS========file:{}===============", file.getName());
                cycleFileContent(file, list);
                log.info("EEEE========file:{}===============", file.getName());

        }
        log.info("=======================");
    }


    public void cycleFileContent(File file, List<String> list) throws Exception {
        if (file.isFile()){
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String lineContent;
            while ((lineContent = bufferedReader.readLine()) != null) {
                for (String content : list) {
                    if (lineContent.contains(content) && !existList.contains(content)) {
                        existList.add(content);
                        log.info("匹配到内容{}，路径为：{}", content, file.getPath());
                    }
                }
            }
            inputReader.close();
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


    public List<String> readList(File file) throws Exception {
        List<String> list = new ArrayList<>();
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String lineContent;
        while ((lineContent = bufferedReader.readLine()) != null) {
            list.add(lineContent);
        }
        return list;
    }


}
