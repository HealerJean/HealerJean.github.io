package com.healerjean.proj.controller;


import com.healerjean.proj.common.dto.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author HealerJean
 * @version 1.0v
 * @Description 字典数据的维护
 * @ClassName A1
 * @date 2019/4/12  17:45.
 */
@RestController
@RequestMapping("/api/sys")
@Slf4j
public class DictionaryController   {


    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 上传地区信息字典
     */
    @PostMapping("district/jsonUpload")
    public ResponseBean districtUpload(MultipartFile file) throws IOException {
        log.info("字典数据--------上传地区信息字典");
        String districtJson  = IOUtils.toString(file.getInputStream(), "utf-8");
         dictionaryService.importDistrictJson(districtJson);
        return ResponseBean.buildSuccess();
    }


    /**
     * 上传地区信息字典
     */
    @GetMapping("district/jsonExport")
    public void districtEport(HttpServletResponse response) throws IOException {
        log.info("字典数据--------上传地区信息字典");
        response.setHeader("Content-Disposition", "inline;filename=district.json");
        IOUtils.copy(IOUtils.toInputStream(dictionaryService.exportDistrictJson()), response.getOutputStream());
    }
}
