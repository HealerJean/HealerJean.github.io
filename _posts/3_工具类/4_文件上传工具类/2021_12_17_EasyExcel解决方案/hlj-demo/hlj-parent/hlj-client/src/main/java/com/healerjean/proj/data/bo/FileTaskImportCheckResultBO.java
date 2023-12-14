package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * FileTaskImportCheckResultBO
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class FileTaskImportCheckResultBO<Excel> implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5763906879585498801L;


    /**
     * 校验结果
     */
    private Boolean checkFlag;

    /**
     * 上传结果描述
     */
    private String resultDesc;

    /**
     * 上传需要导入的数据
     */
    private List<Excel> importExcelList;

    /**
     * 上传结果Excel
     */
    private List<Excel> excelResultList;

}
