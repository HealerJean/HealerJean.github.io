package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * FileTaskCheckResultBO
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class FileTaskExportCheckResultBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1852568103843446332L;

    /**
     * 校验结果
     */
    private Boolean checkFlag;

    /**
     * 错误原因
     */
    private String errorMsg;

    /**
     * success
     *
     * @return {@link FileTaskExportCheckResultBO}
     */
    public static FileTaskExportCheckResultBO success() {
        return new FileTaskExportCheckResultBO().setCheckFlag(true);
    }


    /**
     * fail
     *
     * @param errorMsg errorMsg
     * @return {@link FileTaskExportCheckResultBO}
     */
    public static FileTaskExportCheckResultBO fail(String errorMsg) {
        return new FileTaskExportCheckResultBO().setCheckFlag(false).setErrorMsg(errorMsg);
    }
}
