package com.hlj.util.Z016_Http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author tongdong
 * @Date: 2019/9/18
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpFileEntity {

    /** 文件名 */
    private String fileKey;
    /** 文件类型 */
    private String mime;
    /** 文件 */
    private File file;
}
