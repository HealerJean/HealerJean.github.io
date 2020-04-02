package com.healerjean.proj.util.file;


import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

@Slf4j
public class ZipUtils {

    /**
     * 压缩目录
     */
    public static void compress(String directoryPath) {
        compress(directoryPath, directoryPath + ".zip");
    }

    /**
     * 压缩目录
     *
     * @param directoryPath 源目录
     * @param zipFilePath   目标压缩文件
     */
    public static void compress(String directoryPath, String zipFilePath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            log.info("需要被压缩的路径：{}不存在", directoryPath);
            throw new RuntimeException(directoryPath + "不存在！");
        }

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        File zipFile = new File(zipFilePath);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(directory);
        //fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
        //fileSet.setExcludes(...); //排除哪些文件或文件夹
        zip.addFileset(fileSet);
        zip.execute();
    }

}
