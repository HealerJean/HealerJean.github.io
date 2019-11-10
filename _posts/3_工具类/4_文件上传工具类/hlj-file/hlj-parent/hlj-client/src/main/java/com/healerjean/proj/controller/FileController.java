package com.healerjean.proj.controller;

import com.healerjean.proj.common.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

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
        //1、确定文件存储目录
        String javaIoTmpdir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(javaIoTmpdir);
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


}
