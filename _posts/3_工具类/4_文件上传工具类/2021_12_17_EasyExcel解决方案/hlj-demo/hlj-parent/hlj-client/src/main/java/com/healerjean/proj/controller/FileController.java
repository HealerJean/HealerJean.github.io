package com.healerjean.proj.controller;

import com.healerjean.proj.common.data.bo.BaseRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangyujin
 * @date 2023/6/21$  14:01$
 */
@RestController
@RequestMapping("hlj/file")
@Api(tags = "文件-控制器")
@Slf4j
public class FileController {


    @ApiOperation("上传")
    @PostMapping(value = "upload")
    public BaseRes<Boolean> upload(MultipartFile file) {
        return null;
    }



    @ApiOperation("下载")
    @PostMapping(value = "download")
    public BaseRes<Boolean> download(MultipartFile file) {
        return null;
    }
}
