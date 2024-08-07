package com.healerjean.proj.controller;

import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.common.exception.ParameterErrorException;
import com.healerjean.proj.util.EmptyUtil;
import com.healerjean.proj.util.FileUploadContentTypeFilterUtils;
import com.healerjean.proj.util.file.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author HealerJean
 * @ClassName FileController
 * @date 2019/11/8  16:49.
 * @Description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "文件管理")
@RestController
@RequestMapping("hlj/file")
@Slf4j
public class FileController {

    @ApiOperation(
            value = "文件上传",
            notes = "文件上传",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String upload(MultipartFile file) {
        log.info("文件管理--------文件上传--------请求参数{}", file);

        try {
            boolean flag = FileUploadContentTypeFilterUtils.checkContentType(file.getInputStream());
            if (!flag) {
                throw new ParameterErrorException("该文件类型不允许上传");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //1、确定文件存储目录
        String javaIoTmpdir = System.getProperty("java.io.tmpdir");
        File tempFile = new File("D:");
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        // 2、文件上传
        String fileName = file.getOriginalFilename();
        File outFile = new File(tempFile, fileName);
        // FileOutputStream fileOutputStream = null;
        try {
            // 1、inputstream -> 本地文件
            FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);

            // 2、MultipartFile文件  -> 本地文件
            // file.transferTo(outFile);

            // 3、MultipartFile 文件获取字节 -> OutputStream
            // byte[] bytes = file.getBytes();
            // fileOutputStream = (new FileOutputStream(outFile));
            // fileOutputStream.write(bytes);

            // 4、InputStream -> OutputStream
            // inputStream = file.getInputStream();
            // fileOutputStream = (new FileOutputStream(outFile));
            // IOUtils.copy(inputStream, fileOutputStream);

            log.info("文件管理--------文件上传成功--------上传文件名{}", file.getOriginalFilename());
        } catch (IOException e) {
            log.info("文件上传失败");
            throw new RuntimeException("文件上传失败", e);
        } finally {
            log.info("准备开始关闭流");
            // try {
            //     if (fileOutputStream != null) {
            //         fileOutputStream.close();
            //     }
            // } catch (IOException e) {
            //     log.error("流关闭失败", e);
            // }
            // try {
            //     if (inputStream != null) {
            //         inputStream.close();
            //     }
            // } catch (IOException e) {
            //     log.error("流关闭失败", e);
            // }
        }
        return fileName;
    }


    @ApiOperation(
            value = "文件下载",
            notes = "文件下载",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "download/{fileName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downLoad(HttpServletResponse response, @PathVariable String fileName, Boolean preview) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            log.info("文件管理--------文件下载--------请求参数{}", fileName);
            String javaIoTmpdir = System.getProperty("java.io.tmpdir");
            File file = new File(javaIoTmpdir, fileName);
            if (!file.exists()) {
                throw new BusinessException("文件不存在");
            }
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            if (preview != null && !preview) {
                //强制浏览器下载
                log.info("文件管理--------强制浏览器下载--------文件名{}", fileName);
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } else {
                log.info("文件管理--------文件预览--------文件名{}", fileName);
                //浏览器尝试打开,支持office online或浏览器预览pdf功能
                response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.info("文件：{}，下载失败", fileName, e);
            throw new RuntimeException("文件上传失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream未正确关闭");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream未正确关闭");
                }
            }
        }
    }


    @ApiOperation(
            value = "zip文件下载",
            notes = "zip文件下载",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "zipDownload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void zipDownload(HttpServletResponse response, String fileDir) throws IOException {
        log.info("文件管理--------zip文件下载--------请求参数{}", fileDir);
        File dirFile = new File(fileDir);
        if (!dirFile.exists()) {
            throw new BusinessException("文件不存在");
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(dirFile.getName() + ".zip", "UTF-8"));
        List<Pair<String, InputStream>> filePairs = getFilePairs(dirFile);
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (Pair<String, InputStream> pair : filePairs) {
            ZipEntry zipEntry = new ZipEntry(pair.getLeft());
            zipOut.putNextEntry(zipEntry);
            InputStream inputStream = pair.getRight();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                zipOut.write(bytes, 0, length);
            }
            inputStream.close();
        }
        zipOut.close();
    }

    private List<Pair<String, InputStream>> getFilePairs(File dirFile) throws FileNotFoundException {
        List<Pair<String, InputStream>> pairList = new ArrayList<>();
        File[] files = dirFile.listFiles();
        for (File file : files) {
            Pair<String, InputStream> pair = Pair.of(file.getName(), new FileInputStream(file));
            pairList.add(pair);
        }
        return pairList;
    }


    @ApiOperation(
            value = "zip文件下载2",
            notes = "zip文件下载2",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "zipDownload2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void zipDownload2(HttpServletResponse response, String fileDir) {
        log.info("文件管理--------zip文件下载2--------请求参数{}", fileDir);
        File dirFile = new File(fileDir);
        if (!dirFile.exists()) {
            throw new BusinessException("文件不存在");
        }

        //2、压缩
        ZipUtils.compress(fileDir);
        //3、压缩文件下载
        File file = new File(fileDir + ".zip");
        InputStream inputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName() + ".zip", "UTF-8"));
            inputStream = new FileInputStream(file);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("文件下载异常", e);
            throw new RuntimeException("文件下载异常");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream流关闭失败", e);
                }
            }
        }

    }


}
