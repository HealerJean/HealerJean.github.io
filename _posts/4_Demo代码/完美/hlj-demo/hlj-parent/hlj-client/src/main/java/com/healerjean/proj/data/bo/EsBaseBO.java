package com.healerjean.proj.data.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * EsBaseBO
 *
 * @author zhangyujin
 * @date 2024/4/26
 */

@Data
public class EsBaseBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1091470536422707554L;

    /**
     * uuid
     */
    private String uuid;
}
