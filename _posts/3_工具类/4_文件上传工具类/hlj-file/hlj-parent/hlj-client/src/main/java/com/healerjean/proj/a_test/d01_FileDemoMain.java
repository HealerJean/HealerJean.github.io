package com.healerjean.proj.a_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.util.UUID;

@Slf4j
public class d01_FileDemoMain {


    /**
     * 1、创建文件
     * 1.1、路径不存在，fileNameTxt.createNewFile() 会抛出异常
     * 1.2、只有路径，不会创建出文件，也不会报错，无法创建，但是不会报错
     * 1.3、路径存在，文件不存在，创建成功
     * 1.4、没有路径，只有文件文件名，和则创建的文件和src一个级别，创建成功
     */
    @Test
    public void createFile() throws IOException {
        String filePath = "newFile.txt";
        File fileNameTxt = new File(filePath);
        if (!fileNameTxt.exists()) {
            fileNameTxt.createNewFile();
        }
        log.info("创建成功");
    }


    /**
     * 2、具备目录，创建文件
     * 通过父的目录引入文件   directoryName 是父类
     */
    @Test
    public void createHavaDirectoryFile() throws IOException {
        String directoryName = "D:/test/file/d02_createHavaDirectoryFile";
        String fileName = "directoryName.txt";
        File directoryNameTxt = new File(directoryName, fileName);
        if (!directoryNameTxt.exists()) {
            directoryNameTxt.createNewFile();
        }
        log.info("创建有目录的文件成功");
    }


    /**
     * 3、 判断文件、目录
     */
    @Test
    public void judgeFileOrDirectory() {
        File file = new File("D:/test/file/d02_createHavaDirectoryFile");
        log.info("测试开始");
        if (file.isFile()) {
            log.info(file.getPath() + "是一个文件");
        } else if (file.isDirectory()) {
            log.info(file.getPath() + "是一个目录");
        } else {
            log.info(file.getPath() + "不是文件也不是目录");
        }
    }


    /**
     * 4、创建目录
     * 判断是不是目录
     * 创建目录，即使路径不存在，也会创建相关路径，因为是mkdirs
     */
    @Test
    public void createDirectory() {
        //引入目录
        File directoryName = new File("D:/test/healerjean/file");
        if (!directoryName.exists()) {
            directoryName.mkdirs();
            log.info(directoryName.getPath() + "创建目录成功");
        }
    }


    /**
     * 5、复制文件
     * Input第一个文件以及路径必须存在， 否则 FileOutputStream out = new FileOutputStream(outFilePath); FileNotFoundException 异常
     * Output第二个文件可以不存在,但是路径必须存在，如果路径不存在则FileOutSteam会报错
     */
    public void copyFile() throws IOException {
        String inFilePath = "D:/test/file/d03_copyFile/exist/file.txt";
        String outFilePath = "D:/test/file/d03_copyFile/no_exist/newfile.txt";

        FileInputStream ins = new FileInputStream(inFilePath);
        FileOutputStream out = new FileOutputStream(outFilePath);
        byte[] b = new byte[1024];
        int n = 0;
        while ((n = ins.read(b)) != -1) {
            out.write(b, 0, n);
        }
        ins.close();
        out.close();
        log.info("复制文件成功");
    }


    /**
     * 6.1、递归遍历整个目录的文件
     * file.listFiles()是获取file这个对象也就是file这个目录下面的文件和文件夹的集合
     */
    public void cycleFiles(File file) {
        File[] files = file.listFiles();
        for (File sonFile : files) {
            if (sonFile.isDirectory()) {
                cycleFiles(sonFile);
            } else {
                log.info(sonFile.getAbsolutePath());
            }
        }
        log.info(file.getAbsolutePath());
    }

    @Test
    public void testCycleFiles() {
        File file = new File("D:/test");
        cycleFiles(file);
    }


    /**
     * 6.2 、递归目录，读取文件内容进行匹配
     * size ：用于保留统计记录的个数
     */
    private static int size = 1;

    public void cycleFileContent(File file, String content) throws Exception {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                cycleFileContent(f, content);
            } else {
                String path = f.getPath();
                FileInputStream fileInputStream = new FileInputStream(f);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                String str = new String(outStream.toByteArray(), "utf-8");
                if (str.contains(content)) {
                    log.info("第【{}】个文件匹配到内容，路径为：{}", size, f.getPath());
                    size++;
                }
            }
        }
    }

    @Test
    public void testCycleFileContent() throws Exception {
        File file = new File("D:/test");
        String content = "co";
        cycleFileContent(file, content);
    }


    /**
     * 7.读取文件内容
     * 7.1、读取整个文件内容，转化为ByteArrayOutputStream读取
     */
    @Test
    public void readFileContent() throws Exception {
        File file = new File("D:\\test\\file\\file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = fileInputStream.read(buffer)) != -1) {
            byteOutputStream.write(buffer, 0, len);
        }
        String txtValue = new String(byteOutputStream.toByteArray(), "utf-8");
        log.info(txtValue);
    }


    /**
     * 7.读取文件内容
     * 7.2、按照行,一行一行读取txt内容
     */
    @Test
    public void readFileLineContent() throws Exception {
        FileInputStream inputStream = new FileInputStream("/Users/healerjean/Desktop/logs/hlj-log4j.log");
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String lineContent = null;
        int line = 0;
        while ((lineContent = bufferedReader.readLine()) != null) {
            line++;
            log.info("第【{}】行的内容为：{}", line, lineContent);
        }
    }


    /**
     * 8、根据内容生成文件
     */
    @Test
    public void contentToFile() throws Exception {
        String content = "我是大好人";
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".txt";
        FileOutputStream outputStream = new FileOutputStream("D:/test/" + fileName);
        byte[] buffer = content.getBytes("utf-8");
        outputStream.write(buffer);
        log.info("文件【{}】创建成功", fileName);
        outputStream.close();
    }


    /**
     * 9、File.createTempFile 所在目录 C:\Users\HealerJean\AppData\Local\Temp
     * 注意：1、 prefix必须大于3个字符，2、suffix需要带上 . , 比如：.png、.zip
     *
     */
    @Test
    public void test() {
        try {
            //创建文件 a_name.4788216370145255403.jpg ,中间是随机生成的
            // File jpgFile = File.createTempFile("a_name.", ".jpg");


            //创建目录 scf.contract.4137975757793800315.dir
            File directory = File.createTempFile("scf.contract.", ".dir");
            directory.delete();
            directory.mkdirs();

            //指定目录中创建文件
            File pdfFile = File.createTempFile("scf.contract.", ".pdf", new File("C:\\Users\\HealerJean\\AppData\\Local\\Temp\\healerjean"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
