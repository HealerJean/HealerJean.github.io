package com.healerjean.proj.util.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

@Slf4j
public class FtpUtil {

    private FTPClient ftpClient;

    public static void main(String[] args) throws Exception {
        FtpUtil t = new FtpUtil();
        t.connect("E:\resourse", "10.3.250.74", 21, "HealerJean", "147094");
        File file = new File("D:/test");
        t.upload(file);
    }


    /**
     * @param path     上传到ftp服务器哪个路径下
     * @param addr     地址
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    private boolean connect(String path, String addr, int port, String username, String password) throws Exception {
        boolean result = false;
        ftpClient = new FTPClient();
        int reply;
        //连接ftp  默认是21 不写port也可以
        ftpClient.connect(addr, port);
        ftpClient.connect(addr);
        //ftp登录
        ftpClient.login(username, password);
        //文件类型为二进制文件
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftpClient.getReplyCode();
        //保存到ftp路径下
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return result;
        }
        ftpClient.changeWorkingDirectory(path);
        result = true;

        return result;
    }


    /**
     * 关闭FTP连接
     */
    private void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("关闭FTP连接失败", e);
            }
        }
    }


    /**
     * 上传
     */
    private void upload(File file) throws Exception {
        if (file.isDirectory()) {
            ftpClient.makeDirectory(file.getName());
            ftpClient.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath() + "\\" + files[i]);
                if (file1.isDirectory()) {
                    upload(file1);
                    //上传目录
                    ftpClient.changeToParentDirectory();
                } else {
                    File file2 = new File(file.getPath() + "\\" + files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    //上传文件
                    ftpClient.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        } else {
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            //上传文件
            ftpClient.storeFile(file2.getName(), input);
            input.close();
        }
    }


    /**
     * 下载该目录下所有文件到本地
     * @param ftpPath  FTP服务器上的相对路径，例如：test/123
     * @param savePath 保存文件到本地的路径，例如：D:/test
     */
    public void downloadFiles(String ftpPath, String savePath) {
        // 登录
        if (ftpClient != null) {
            try {
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                    throw new RuntimeException("ftp目录不存在");
                }
                // 设置被动模式，开通一个端口来传输数据
                ftpClient.enterLocalPassiveMode();
                String[] listNames = ftpClient.listNames();
                // 判断该目录下是否有文件
                if (listNames == null || listNames.length == 0) {
                    throw new RuntimeException("ftp目录下文件不存在");
                }
                for (String fileNmae : listNames) {
                    File file = new File(savePath + '/' + fileNmae);
                    try (OutputStream os = new FileOutputStream(file)) {
                        ftpClient.retrieveFile(fileNmae, os);
                    } catch (Exception e) {
                        throw new RuntimeException("文件下载出错", e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("文件下载出错", e);
            }
        }
    }


}
