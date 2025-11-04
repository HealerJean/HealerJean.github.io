package com.healerjean.proj.utils.gray;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * GrayInsuranceBusinessDto
 * @author zhangyujin
 * @date 2022/9/22  13:48.
 */
@Accessors(chain = true)
@Data
public class GrayBizBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5110796299306482078L;

    /**
     * 灰度白名单
     */
    private Set<String> whiteInfos;

    /**
     * 灰度黑名单
     */
    private Set<String> blackInfos;

    /**
     * 灰度比例
     */
    private Long rate;

    /**
     * 灰度总额
     */
    private Long amount;



}
