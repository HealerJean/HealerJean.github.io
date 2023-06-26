package com.healerjean.proj.util.file;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

@Component
@Slf4j
public class SFTPUtil {

    private static final String encoding = "UTF-8";

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private String portStr;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remotePath}")
    private String remotePath;

    @Value("${sftp.rsa}")
    private String rsa;


    /**
     * 使用配置文件连接
     */
    private ChannelSftp connect() {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            if (StringUtils.isNotBlank(rsa)) {
                jsch.addIdentity(rsa);
            }
            int port = Integer.valueOf(portStr);
            Session sshSession = jsch.getSession(username, host, port);
            log.info("Session created");
            if (StringUtils.isBlank(rsa)) {
                sshSession.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.info("Session connected");
            log.info("Opening Channel");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("Connected to {}", host);
        } catch (Exception e) {
            log.info("Connected to {} failed", host, e);
            throw new RuntimeException(e.getMessage());
        }
        return sftp;
    }

    /**
     * 手动连接SFTP
     */
    private ChannelSftp handConnect(String rsa, String host, String portStr, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            if (StringUtils.isNotBlank(rsa)) {
                jsch.addIdentity(rsa);
            }
            int port = Integer.valueOf(portStr);
            Session sshSession = jsch.getSession(username, host, port);
            log.info("Session created");
            if (StringUtils.isBlank(rsa)) {
                sshSession.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.info("Session connected");
            log.info("Opening Channel");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("Connected to {}", host);
        } catch (Exception e) {
            log.info("Connected to {} failed", host, e);
            throw new RuntimeException(e.getMessage());
        }
        return sftp;
    }

    /**
     * 断开连接
     */
    private void disconnect(ChannelSftp sftp) {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is disconnecting");
            } else if (sftp.isClosed()) {
                log.info("sftp is already closed");
            }
            try {
                if (sftp.getSession().isConnected()) {
                    sftp.getSession().disconnect();
                }
            } catch (Exception e) {
                log.error("sftp  session is disconnect", e);
                throw new RuntimeException(e.getMessage());
            }
        }

    }


    /**
     * 去读Sfto文件
     *
     * @param directory      目录
     * @param remoteFileName 远程文件名
     * @return
     */
    public String readSFTPFile(String directory, String remoteFileName) {
        ChannelSftp sftp = connect();
        return readSFTPFile(remotePath + directory, remoteFileName, sftp);
    }

    /**
     * @param remoteDirectory 远程目录 比如 /asset/test
     * @param remoteFileName  远程文件名
     * @param sftp
     * @return
     */
    private String readSFTPFile(String remoteDirectory, String remoteFileName, ChannelSftp sftp) {
        if (sftp == null) {
            log.error("connect to sftp failed");
            return "false";
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        InputStream ins = null;

        try {
            sftp.cd(remoteDirectory);
            ins = sftp.get(remoteFileName);
            if (ins == null) {
                log.error("filename is not exit");
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(ins, encoding));
            String inLine = reader.readLine();
            while (inLine != null) {
                builder.append(inLine);
                builder.append("\n");
                inLine = reader.readLine();
            }
        } catch (SftpException se) {
            log.info("read sftp {} error ", remoteFileName, se);
            throw new RuntimeException(se.getMessage());
        } catch (Exception e) {
            log.info("read sftp {} error ", remoteFileName, e);
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                log.error("read ftpFile close  InputStream or BufferedReader error ", e);
                throw new RuntimeException(e.getMessage());
            }
            disconnect(sftp);
        }
        return builder.toString();
    }


    /**
     * 下载单个文件到本地文件
     *
     * @param directory         远程目录
     * @param remoteFileName    远程文件名
     * @param downLoadLocalPath 本地下载后存储路径
     * @param localFileName     本地下载后的文件名
     * @return
     */
    public boolean downloadFile(String directory, String remoteFileName, String downLoadLocalPath, String localFileName) {
        ChannelSftp sftp = this.connect();
        return downloadFile(remotePath + directory, remoteFileName, downLoadLocalPath, localFileName, sftp);
    }

    /**
     * 下载单个文件到本地文件
     *
     * @param remoteDirectory   远程目录 比如 /asset/test
     * @param remoteFileName    远程文件名
     * @param downLoadLocalPath 本地下载后存储路径
     * @param localFileName     本地下载后的文件名
     * @return
     */
    private boolean downloadFile(String remoteDirectory, String remoteFileName, String downLoadLocalPath, String localFileName, ChannelSftp sftp) {
        FileOutputStream out = null;
        try {
            File file = new File(downLoadLocalPath, localFileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            sftp.get(remoteDirectory + "/" + remoteFileName, out);
            log.info("down load single file success , {}", remoteFileName);
            return true;
        } catch (Exception e) {
            log.error("get file error", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("download single file close error", e);
                    throw new RuntimeException(e.getMessage());
                }
            }
            this.disconnect(sftp);
        }
    }


    /**
     * 批量下载文件
     *
     * @param remoteDirectory   远程下载的目录
     * @param downLoadLocalPath 下载的本地路径
     * @param fileStartFormat   文件名开头格式
     * @param del
     * @return
     */
    public boolean batchDownLoadFile(String remoteDirectory, String fileStartFormat, String downLoadLocalPath, boolean del, ChannelSftp sftp) {
        try {
            File directory = new File(downLoadLocalPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Vector v = listFiles(remoteDirectory, sftp);
            if (v.size() > 0) {
                log.info("batch down files start");
                Iterator iterator = v.iterator();
                while (iterator.hasNext()) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) iterator.next();
                    final String filename = lsEntry.getFilename();
                    SftpATTRS attrs = lsEntry.getAttrs();
                    //不是目录
                    if (!attrs.isDir()) {
                        //文件格式
                        if (StringUtils.isNotEmpty(fileStartFormat)) {
                            if (filename.startsWith(fileStartFormat)) {
                                if (downloadFile(remoteDirectory, filename, downLoadLocalPath, filename, sftp) && del) {
                                    deleteSFTPFile(remoteDirectory, filename, sftp);
                                }
                            }
                        } else {
                            if (downloadFile(remoteDirectory, filename, downLoadLocalPath, filename, sftp) && del) {
                                deleteSFTPFile(remoteDirectory, filename, sftp);
                            }
                        }
                    }
                }//while end
            }
        } catch (Exception e) {
            log.error("batch down files error", e);
            return false;
        } finally {
            this.disconnect(sftp);
        }
        return true;
    }


    /**
     * 批量下载文件
     *
     * @param directory         目录
     * @param downLoadLocalPath 下载的本地路径
     * @param fileStartFormat   文件名开头格式
     * @param del
     * @return
     */
    public boolean batchDownLoadFile(String directory, String fileStartFormat, String downLoadLocalPath, boolean del) {
        ChannelSftp sftp = this.connect();
        return batchDownLoadFile(remotePath, fileStartFormat, downLoadLocalPath, del, sftp);
    }


    /**
     * @param remoteDirectory 远程上传目录
     * @param uploadFileName  上传的文件名
     * @param inputStream     上传的流
     * @param sftp
     * @return
     */
    public boolean uploadFile(String remoteDirectory, String uploadFileName, InputStream inputStream, ChannelSftp sftp) {
        boolean flag = false;
        for (int i = 1; i < 3; i++) {
            log.info("开始第" + i + "次上传文件,名称 {}", uploadFileName);
            try {
                createDirAndCd(remoteDirectory, sftp);
                sftp.put(inputStream, uploadFileName);
                flag = true;
                log.info("第" + i + "次上传文件成功,名称 {}", uploadFileName);
                break;
            } catch (Exception e) {
                log.error("occurs exception", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("occurs exception", e);
                    }
                }
                this.disconnect(sftp);
            }
            if (i == 3 && !flag) {
                log.info("上传3次均失败,文件名称 {}", uploadFileName);
            }
        }
        return flag;
    }


    /**
     * @param directory      目录
     * @param remoteFileName 上传的文件名
     * @param inputStream
     * @return
     */
    public boolean uploadFile(String directory, String remoteFileName, InputStream inputStream) {
        boolean flag = false;
        for (int i = 1; i < 4; i++) {
            log.info("开始第" + i + "次上传文件,名称 {}", remoteFileName);
            ChannelSftp sftp = this.connect();
            FileInputStream in = null;
            try {
                createDirAndCd(remotePath + "/" + directory, sftp);
                sftp.put(inputStream, remoteFileName);
                flag = true;
                log.info("第" + i + "次上传文件成功,名称 {}", remoteFileName);
                break;
            } catch (Exception e) {
                log.error("occurs exception", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("occurs exception", e);
                    }
                }
                this.disconnect(sftp);
            }
            if (i == 3 && !flag) {
                log.info("上传3次均失败,商户 {},文件名称 {}", remoteFileName);
                // AlarmMailLog.logTask("上传3次均失败,商户 " + merchantId + ",文件 "+localFileName);
            }
        }//for 循环结束
        return flag;
    }


    /**
     * 创建目录 并切换到创建的目录下面
     */
    private boolean createDirAndCd(String createpath, ChannelSftp sftp) {
        try {
            if (isDirExist(createpath, sftp)) {
                sftp.cd(createpath);
                return true;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(), sftp)) {
                    sftp.cd(filePath.toString());
                    filePath.delete(0, filePath.length());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                    filePath.delete(0, filePath.length());
                }
            }
            return true;
        } catch (SftpException e) {
            log.error("occurs exception", e);
        }
        return false;
    }


    private Vector listFiles(String remoteDirectory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(remoteDirectory);
    }

    public Vector listFiles(String directory) {
        ChannelSftp sftp = this.connect();
        try {
            return listFiles(remotePath + directory, sftp);
        } catch (Exception e) {
            log.error("列举远程目录下文件出错", e);
            return null;
        } finally {
            this.disconnect(sftp);
        }
    }


    private void deleteSFTPFile(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            log.info("delete remote file success {}", deleteFile);
        } catch (Exception e) {
            log.error("delete remote file fail", e);
        }
    }

    /**
     * 判断目录是否存在
     */
    private boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }


    /**
     * 测试
     * 1、连接Sftp
     * 2、读取sftp文件内容
     * 3、下载sftp文件
     * 4、上传文件
     * 5、批量下载文件
     */
    public static void main(String[] args) {
        SFTPUtil sftpUtil = new SFTPUtil();
        //1、连接Sftp
        ChannelSftp sftp = sftpUtil.handConnect("D:/work/sftp/id_rsa", "127.0.0.1", "22", "asset", null);

        //2、读取sftp文件内容
        //远程目录
        // String remoteDirectory = "/asset/test";
        //文件名
        // String remoteFileName = "check_info_result.txt";
        // String content = sftpUtil.readSFTPFile(remoteDirectory,remoteFileName,sftp);
        // System.out.println(content);

        // 3、下载sftp文件
        // String downLoadLocalPath = "D:/work/sftp/temp";
        // String downLoadFileName = "NEW.TXT" ;
        // sftpUtil.downloadFile(remoteDirectory,remoteFileName,downLoadLocalPath,downLoadFileName,sftp);


        // 4、上传文件
        // String uploadFileName = "upload_NEW.TXT";
        // try {
        //     FileInputStream inputStream = new FileInputStream("D:/work/sftp/temp/NEW.TXT");
        //     sftpUtil.uploadFile(remoteDirectory,uploadFileName,inputStream,sftp);
        // } catch (FileNotFoundException e) {
        //     e.printStackTrace();
        // }


        //5、批量下载文件
        // String downLoadLocalPath = "D:/work/sftp/temp";
        // sftpUtil.batchDownLoadFile(remoteDirectory,"u",downLoadLocalPath,false,sftp);

        //断开连接
        sftpUtil.disconnect(sftp);


    }
}
